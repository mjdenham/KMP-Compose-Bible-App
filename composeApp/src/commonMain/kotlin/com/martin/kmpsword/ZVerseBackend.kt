package com.martin.kmpsword

import com.martin.kmpsword.passage.Key
import com.martin.kmpsword.passage.Verse
import com.martin.kmpsword.sword.SwordConstants
import com.martin.kmpsword.sword.SwordUtil
import okio.Buffer
import okio.FileHandle
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM


class ZVerseBackend: AbstractBackend() {

    private var active: Boolean = false

    /**
     * The array of index random access files
     */
    private val idxRaf: Array<FileHandle?> = arrayOfNulls(3)

    /**
     * The array of data random access files
     */
    private val textRaf: Array<FileHandle?> = arrayOfNulls(3)

    /**
     * The array of compressed random access files
     */
    private val compRaf: Array<FileHandle?> = arrayOfNulls(3)

    private var lastBlockNum: Int = 0
    private var lastTestament: Int = 0
    private var lastUncompressed: ByteArray? = null

    fun activate(/*lock: Lock?*/) {
        try {
            if (idxRaf[SwordConstants.TESTAMENT_OLD] == null) {
                val path: Path = getExpandedDataPath()
                val blockTypeChar = "b" //blockType.getIndicator()

                val otAllButLast: String = path.resolve(SwordConstants.FILE_OT + '.' + blockTypeChar + SUFFIX_PART1).toString()
                idxRaf[SwordConstants.TESTAMENT_OLD] = FileSystem.SYSTEM.openReadOnly((otAllButLast + SUFFIX_INDEX).toPath())
                textRaf[SwordConstants.TESTAMENT_OLD] = FileSystem.SYSTEM.openReadOnly((otAllButLast + SUFFIX_TEXT).toPath())
                compRaf[SwordConstants.TESTAMENT_OLD] = FileSystem.SYSTEM.openReadOnly((otAllButLast + SUFFIX_COMP).toPath())

                val ntAllButLast: String = path.resolve(SwordConstants.FILE_NT + '.' + blockTypeChar + SUFFIX_PART1).toString()
                idxRaf[SwordConstants.TESTAMENT_NEW] = FileSystem.SYSTEM.openReadOnly((ntAllButLast + SUFFIX_INDEX).toPath())
                textRaf[SwordConstants.TESTAMENT_NEW] = FileSystem.SYSTEM.openReadOnly((ntAllButLast + SUFFIX_TEXT).toPath())
                compRaf[SwordConstants.TESTAMENT_NEW] = FileSystem.SYSTEM.openReadOnly((ntAllButLast + SUFFIX_COMP).toPath())
            }
        } catch (e: /*Book*/Exception) {
//            idxFile.get(SwordConstants.TESTAMENT_OLD) = null
//            textFile.get(SwordConstants.TESTAMENT_OLD) = null
//            compFile.get(SwordConstants.TESTAMENT_OLD) = null
//
//            idxFile.get(SwordConstants.TESTAMENT_NEW) = null
//            textFile.get(SwordConstants.TESTAMENT_NEW) = null
//            compFile.get(SwordConstants.TESTAMENT_NEW) = null
            e.printStackTrace()

            return
        }

//        if (idxFile.get(SwordConstants.TESTAMENT_OLD).canRead()) {
//            try {
//                idxRaf[SwordConstants.TESTAMENT_OLD] = java.io.RandomAccessFile(
//                    idxFile.get(SwordConstants.TESTAMENT_OLD),
//                    FileUtil.MODE_READ
//                )
//                textRaf[SwordConstants.TESTAMENT_OLD] = java.io.RandomAccessFile(
//                    textFile.get(SwordConstants.TESTAMENT_OLD),
//                    FileUtil.MODE_READ
//                )
//                compRaf[SwordConstants.TESTAMENT_OLD] = java.io.RandomAccessFile(
//                    compFile.get(SwordConstants.TESTAMENT_OLD),
//                    FileUtil.MODE_READ
//                )
//            } catch (ex: java.io.FileNotFoundException) {
//                assert(false) { ex }
//                ZVerseBackend.log.error("Could not open OT", ex) //$NON-NLS-1$
//                idxRaf[SwordConstants.TESTAMENT_OLD] = null
//                textRaf[SwordConstants.TESTAMENT_OLD] = null
//                compRaf[SwordConstants.TESTAMENT_OLD] = null
//            }
//        }

//        if (idxFile.get(SwordConstants.TESTAMENT_NEW).canRead()) {
//            try {
//                idxRaf[SwordConstants.TESTAMENT_NEW] = java.io.RandomAccessFile(
//                    idxFile.get(SwordConstants.TESTAMENT_NEW),
//                    FileUtil.MODE_READ
//                )
//                textRaf[SwordConstants.TESTAMENT_NEW] = java.io.RandomAccessFile(
//                    textFile.get(SwordConstants.TESTAMENT_NEW),
//                    FileUtil.MODE_READ
//                )
//                compRaf[SwordConstants.TESTAMENT_NEW] = java.io.RandomAccessFile(
//                    compFile.get(SwordConstants.TESTAMENT_NEW),
//                    FileUtil.MODE_READ
//                )
//            } catch (ex: java.io.FileNotFoundException) {
//                assert(false) { ex }
//                ZVerseBackend.log.error("Could not open NT", ex) //$NON-NLS-1$
//                idxRaf[SwordConstants.TESTAMENT_NEW] = null
//                textRaf[SwordConstants.TESTAMENT_NEW] = null
//                compRaf[SwordConstants.TESTAMENT_NEW] = null
//            }
//        }
//
        active = true
    }

    private fun checkActive() {
        if (!active) {
            activate()
        }
    }


    fun getRawText(key: Key): String {
        checkActive()

//        val sbmd: SwordBookMetaData = getBookMetaData()
//        val charset: String = sbmd.getBookCharset()

//        val verse: Verse = KeyUtil.getVerse(key)
        val verse = key as Verse

        try {
            val testament: Int = SwordConstants.getTestament(verse)
            val index: Int = SwordConstants.getIndex(verse)
            println("Verse index $index")

            // If Bible does not contain the desired testament, return nothing.
            val idxFile = idxRaf[testament]
                ?: return "" //$NON-NLS-1$

            // 10 because the index is 10 bytes long for each verse
            var temp: Buffer = SwordUtil.readFile(
                idxFile,
                index * IDX_ENTRY_SIZE,
                IDX_ENTRY_SIZE,
            )

            // If the Bible does not contain the desired verse, return nothing.
            // Some Bibles have different versification, so the requested verse
            // may not exist.
            if (temp == null || temp.size == 0L) {
                return "" //$NON-NLS-1$
            }

            // The data is little endian - extract the blockNum, verseStart and
            // verseSize
            val blockNum: Int = temp.readIntLe()
            val verseStart: Int = temp.readIntLe()
            val verseSize: Short = temp.readShortLe()
            println("index: $index blockNum: $blockNum verseStart: $verseStart verseSize: $verseSize")

            // Can we get the data from the cache
            val uncompressed: ByteArray
            if (blockNum == lastBlockNum && testament == lastTestament && lastUncompressed != null) {
                uncompressed = lastUncompressed!!
            } else {
                // Then seek using this index into the idx file
                val compFile = compRaf[testament] ?: return ""
                temp = SwordUtil.readFile(
                    compFile,
                    blockNum * COMP_ENTRY_SIZE,
                    COMP_ENTRY_SIZE,
                )
                if (temp == null || temp.size == 0L) {
                    return "" //$NON-NLS-1$
                }

                val blockStart: Int = temp.readIntLe()
                val blockSize: Int = temp.readIntLe()
                val uncompressedSize: Int = temp.readIntLe()
                println("blockStart: $blockStart blockSize: $blockSize uncompressedSize: $uncompressedSize")

                // Read from the data file.
                val textFile = textRaf.get(testament) ?: return ""
                uncompressed = SwordUtil.readAndInflateFile(textFile, blockStart, blockSize, uncompressedSize).readByteArray()

                //TODO decipher(data)
                //TODO allow various compressor types
//                    CompressorType.fromString(sbmd.getProperty(ConfigEntryType.COMPRESS_TYPE))
//                        .getCompressor(data).uncompress(uncompressedSize).toByteArray()

                println("uncompressed size: ${uncompressed.size}")
                // cache the uncompressed data for next time
                lastBlockNum = blockNum
                lastTestament = testament
                lastUncompressed = uncompressed
            }

            // and cut out the required section.
            val chopped = ByteArray(verseSize.toInt())
            uncompressed.copyInto(chopped, 0, verseStart, verseStart + verseSize)

            return SwordUtil.decode(key.toString(), chopped, "UTF-8")
        } catch (e: Exception) { //java.io.IOException) {
            e.printStackTrace()
//            throw BookException(
//                UserMsg.READ_FAIL, e, arrayOf<Any>(
//                    verse.getName()
//                )
//            )
            return "Error reading verse"
        }
    }

    companion object {
        private const val SUFFIX_COMP: String = "s"
        private const val SUFFIX_INDEX: String = "v"
        private const val SUFFIX_PART1: String = "z"
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