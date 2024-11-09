/**
 * Distribution License:
 * JSword is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, version 2.1 or later
 * as published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The License is available on the internet at:
 * http://www.gnu.org/copyleft/lgpl.html
 * or by writing to:
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA
 *
 * © CrossWire Bible Society, 2005 - 2016
 *
 */
package org.crosswire.jsword.book.sword

import okio.FileHandle
import org.crosswire.jsword.book.sword.state.LastLoadedBlock
import org.crosswire.jsword.book.sword.state.OpenFileStateManager
import org.crosswire.jsword.book.sword.state.ZVerseBackendState
import org.crosswire.jsword.passage.Key
import org.crosswire.jsword.passage.KeyUtil
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.Testament
import org.crosswire.jsword.versification.Versification
import org.crosswire.jsword.versification.system.Versifications

/**
 * A backend to read compressed data verse based files. While the text file
 * contains data compressed with ZIP or LZSS, it cannot be uncompressed using a
 * stand alone zip utility, such as WinZip or gzip. The reason for this is that
 * the data file is a concatenation of blocks of compressed data.
 *
 *
 *
 * The blocks can either be "b" book (aka testament); "c" chapter or "v" verse
 * The choice is a matter of trade offs. The program needs to uncompress
 * a block into memory. Having it at the book level is very memory expensive.
 * Having it at the verse level is very disk expensive, but takes the least
 * amount of memory. The most common is chapter.
 *
 *
 *
 *
 * In order to find the data in the text file, we need to find the block. The
 * first index (idx) is used for this. Each verse is indexed to a tuple (block
 * number, verse start, verse size). This data allows us to find the correct
 * block, and to extract the verse from the uncompressed block, but it does not
 * help us uncompress the block.
 *
 *
 *
 *
 * Once the block is known, then the next index (comp) gives the location of the
 * compressed block, its compressed size and its uncompressed size.
 *
 *
 *
 *
 * There are 3 files for each testament, 2 (idx and comp) are indexes into the
 * third (text) which contains the data. The key into each index is the verse
 * index within that testament, which is determined by book, chapter and verse
 * of that key.
 *
 *
 *
 *
 * All unsigned numbers are stored 2-complement, little endian.
 *
 *
 *
 * Then proceed as follows, at all times working on the set of files for the
 * testament in question:
 *
 *
 * The three files are laid out in the following fashion:
 *
 *  * The idx file has one entry per verse in the versification. The number
 * of verses varies by versification and testament. Each entry describes the
 * compressed block in which it is found, the start of the verse in the
 * uncompressed block and the length of the verse.
 *
 *  * Block number - 32-bit/4-bytes - the number of the entry in the comp file.
 *  * Verse start - 32 bit/4-bytes - the start of the verse in the uncompressed block in the dat file.
 *  * Verse length - 16 or 32 bit/2 or 4-bytes - the length of the verse in the uncompressed block from the dat file.
 *
 * Algorithm:
 *
 *  * Given the ordinal value of the verse, seek to the ordinal * entrysize and read entrysize bytes.
 *  * Decode the entrysize bytes as Block Number, Verse start and length
 *
 *
 *  * The comp file has one entry per block.
 * Each entry describes the location of a compressed block,
 * giving its start and size in the next file.
 *
 *  * Block Start - 32-bit/4-byte - the start of the block in the dat file
 *  * Compressed Block Size - 32-bit/4-byte - the size of the compressed block in the dat file
 *  * Uncompressed Block Size - 32-bit/4-byte - the size of the block after uncompressing
 *
 * Algorithm:
 *
 *  * Given a block number, seek to block-index * 12 and read 12 bytes
 *  * Decode the 12 bytes as Block Start, Compressed Block Size and Uncompressed Block Size
 *
 *
 *  *  The dat file is compressed blocks of verses.
 * <br></br>
 * Algorithm:
 *
 *  * Given the entry from the comp file, seek to the start and read the indicated compressed block size
 *  * If the book is enciphered it, decipher it.
 *  * Uncompress the block, using the uncompressed size as an optimization.
 *  * Using the verse start, seek to that location in the uncompressed block and read the indicated verse size.
 *  * Convert the bytes to a String using the books indicated charset.
 *
 *
 *
 *
 * @author Joe Walker
 * @author DM Smith
 */
class ZVerseBackend(val bookMetaData: SwordBookMetaData, val blockType: BlockType, val dataSize: Int) : AbstractBackend<ZVerseBackendState>(bookMetaData) {

    override fun initState(): ZVerseBackendState {
        return OpenFileStateManager.getZVerseBackendState(getBookMetaData(), blockType)
    }

    override fun readRawContent(state: ZVerseBackendState, key: Key): String {
        val charset = "UTF-8" //bookMetaData.getBookCharset();
        val compressType = "ZIP" //bookMetaData.getProperty(SwordBookMetaData.KEY_COMPRESS_TYPE);

        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n: Versification = Versifications.instance().getVersification(v11nName)
        val verse: Verse = KeyUtil.getVerse(key)

        var index = verse.ordinal
        val testament: Testament = v11n.getTestament(index)
        index = v11n.getTestamentOrdinal(index)

        val idxFile: FileHandle? = state.getIdxFile(testament)
        val compFile: FileHandle? = state.getCompFile(testament)
        val textFile: FileHandle? = state.getTextFile(testament)

        // If Bible does not contain the desired testament, return nothing.
        if (idxFile == null || compFile == null || textFile == null) {
            return ""
        }

        // entrysize because the index is entrysize bytes long for each verse
        var temp: ByteArray = SwordUtil.readFile(
            idxFile,
            index * IDX_ENTRY_SIZE,
            IDX_ENTRY_SIZE,
        )
        // If the Bible does not contain the desired verse, return nothing.
        // Some Bibles have different versification, so the requested verse
        // may not exist.
        if (temp == null || temp.size == 0) {
            return ""
        }

        // The data is little endian - extract the blockNum, verseStart and verseSize
        val blockNum: Int = SwordUtil.decodeLittleEndian32(temp, 0)
        val verseStart: Int = SwordUtil.decodeLittleEndian32(temp, 4)
        val verseSize: Int = if (dataSize == 2) {
            SwordUtil.decodeLittleEndian16(temp, 8)
        } else { // dataSize == 4:
            SwordUtil.decodeLittleEndian32(temp, 8)
        }

        // Can we get the data from the cache
        val uncompressed: ByteArray?
        val lastLoadedBlock = state.lastLoadedBlock
        if (lastLoadedBlock != null && blockNum == lastLoadedBlock.blockNum && testament == lastLoadedBlock.testament) {
            uncompressed = lastLoadedBlock.uncompressed
        } else {
            // Then seek using this index into the idx file
            temp = SwordUtil.readFile(compFile, blockNum * COMP_ENTRY_SIZE, COMP_ENTRY_SIZE)
            if (temp.isEmpty()) {
                return ""
            }

            val blockStart: Int = SwordUtil.decodeLittleEndian32(temp, 0)
            val blockSize: Int = SwordUtil.decodeLittleEndian32(temp, 4)
            val uncompressedSize: Int = SwordUtil.decodeLittleEndian32(temp, 8)
            println("blockStart: $blockStart blockSize: $blockSize uncompressedSize: $uncompressedSize")

            // Read from the data file.
            uncompressed = SwordUtil.readAndInflateFile(textFile, blockStart, blockSize, uncompressedSize)

//            decipher(data);
//        uncompressed =
//            CompressorType.fromString(compressType).getCompressor(data).uncompress(uncompressedSize)
//                .toByteArray()

            // cache the uncompressed data for next time
            state.lastLoadedBlock = LastLoadedBlock(testament, blockNum, uncompressed)
        }

        // and cut out the required section.
        val chopped = ByteArray(verseSize)
        uncompressed.copyInto(chopped, 0, verseStart, verseStart + verseSize)

        return SwordUtil.decode(key.getName(), chopped, charset)
    }

    /**
     * The number of bytes for each entry in the index: either 6 or 8
     */
//    protected val entrysize: Int

    companion object {
        private const val SUFFIX_PART1: String = "z"

        private const val SUFFIX_COMP: String = "s"
        private const val SUFFIX_INDEX: String = "v"
        private const val SUFFIX_TEXT: String = "z"

        /**
         * How many bytes in the comp index?
         */
        private const val IDX_ENTRY_SIZE: Int = 10

        /**
         * How many bytes in the idx index?
         */
        const val COMP_ENTRY_SIZE: Int = 12
    }
}
