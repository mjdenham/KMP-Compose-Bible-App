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
 * © CrossWire Bible Society, 2013 - 2016
 *
 */
package org.crosswire.jsword.book.sword.state

import com.martin.kmpsword.sword.SwordConstants
import okio.FileHandle
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.sword.BlockType
import org.crosswire.jsword.book.sword.SwordUtil
import org.crosswire.jsword.versification.Testament

/**
 * Stores the random access files required for processing the passage request.
 *
 * The caller is required to close to correctly free resources and avoid File
 * pointer leaks.
 *
 * @see gnu.lgpl.License The GNU Lesser General Public License for details.
 *
 * @author DM Smith
 */
class ZVerseBackendState internal constructor(bookMetaData: BookMetaData, blockType: BlockType) :
    AbstractOpenFileState(bookMetaData) {
    override fun releaseResources() {
        ntIdxFile?.close()
        ntCompFile?.close()
        ntTextFile?.close()
        otIdxFile = null
        otCompFile = null
        otTextFile = null
        ntIdxFile = null
        ntCompFile = null
        ntTextFile = null
    }

    /**
     * Get the index file for the given testament.
     *
     * @param testament the testament for the index
     * @return the index for the testament
     */
    fun getIdxFile(testament: Testament): FileHandle? {
        return if (testament == Testament.NEW) ntIdxFile else otIdxFile
    }

    /**
     * Get the compression file for the given testament.
     *
     * @param testament the testament for the index
     * @return the index for the testament
     */
    fun getCompFile(testament: Testament): FileHandle? {
        return if (testament == Testament.NEW) ntCompFile else otCompFile
    }

    /**
     * Get the text file for the given testament.
     *
     * @param testament the testament for the index
     * @return the index for the testament
     */
    fun getTextFile(testament: Testament): FileHandle? {
        return if (testament == Testament.NEW) ntTextFile else otTextFile
    }

    /**
     * The index random access files
     */
    private var otIdxFile: FileHandle? = null
    private var ntIdxFile: FileHandle? = null

    /**
     * The compressed random access files
     */
    private var otCompFile: FileHandle? = null
    private var ntCompFile: FileHandle? = null

    /**
     * The data random access files
     */
    private var otTextFile: FileHandle? = null
    private var ntTextFile: FileHandle? = null

    /**
     * the last Testament
     * @return the lastTestament
     */
    var lastTestament: Testament? = null
    /**
     * the lastBlockNum
     * @param lastBlockNum
     */
    var lastBlockNum: Int = -1
    /**
     * the lastUncompressed
     * @param lastUncompressed
     */
    var lastUncompressed: ByteArray? = null

    /**
     * This is default package access for forcing the use of the
     * OpenFileStateManager to manage the creation. Not doing so may result in
     * new instances of OpenFileState being created for no reason, and as a
     * result, if they are released to the OpenFileStateManager by mistake this
     * would result in leakage
     *
     * @param bookMetaData the appropriate metadata for the book
     */
    init {
        val path = SwordUtil.getExpandedDataPath(bookMetaData)
        val otAllButLast = path.resolve(SwordConstants.FILE_OT + '.' + blockType.indicator + SUFFIX_PART1).toString()
//            "/Users/martin/StudioProjects/kmp-sword/testFiles/BSB/modules/texts/ztext/bsb/ot.bz"
        otIdxFile = FileSystem.SYSTEM.openReadOnly((otAllButLast + SUFFIX_INDEX).toPath())
        otTextFile = FileSystem.SYSTEM.openReadOnly((otAllButLast + SUFFIX_TEXT).toPath())
        otCompFile = FileSystem.SYSTEM.openReadOnly((otAllButLast + SUFFIX_COMP).toPath())

        val ntAllButLast = path.resolve(SwordConstants.FILE_NT + '.' + blockType.indicator + SUFFIX_PART1).toString()
//            "/Users/martin/StudioProjects/kmp-sword/testFiles/BSB/modules/texts/ztext/bsb/nt.bz"
        ntIdxFile = FileSystem.SYSTEM.openReadOnly((ntAllButLast + SUFFIX_INDEX).toPath())
        ntTextFile = FileSystem.SYSTEM.openReadOnly((ntAllButLast + SUFFIX_TEXT).toPath())
        ntCompFile = FileSystem.SYSTEM.openReadOnly((ntAllButLast + SUFFIX_COMP).toPath())


//        val path: java.net.URI = SwordUtil.getExpandedDataPath(bookMetaData)
//        val otAllButLast: String = NetUtil.lengthenURI(
//            path,
//            (java.io.File.separator + SwordConstants.FILE_OT + '.' + blockType.indicator).toString() + SUFFIX_PART1
//        ).getPath()
//        val otIdxFile: java.io.File = java.io.File(otAllButLast + SUFFIX_INDEX)
//        val otTextFile: java.io.File = java.io.File(otAllButLast + SUFFIX_TEXT)
//        val otCompFile: java.io.File = java.io.File(otAllButLast + SUFFIX_COMP)
//
//        val ntAllButLast: String = NetUtil.lengthenURI(
//            path,
//            (java.io.File.separator + SwordConstants.FILE_NT + '.' + blockType.indicator).toString() + SUFFIX_PART1
//        ).getPath()
//        val ntIdxFile: java.io.File = java.io.File(ntAllButLast + SUFFIX_INDEX)
//        val ntTextFile: java.io.File = java.io.File(ntAllButLast + SUFFIX_TEXT)
//        val ntCompFile: java.io.File = java.io.File(ntAllButLast + SUFFIX_COMP)
//
//        // check whether exists to swallow any exception as before
//        if (otIdxFile.canRead()) {
//            try {
//                otCompRaf = java.io.RandomAccessFile(otIdxFile, FileUtil.MODE_READ)
//                otTextRaf = java.io.RandomAccessFile(otTextFile, FileUtil.MODE_READ)
//                otIdxRaf = java.io.RandomAccessFile(otCompFile, FileUtil.MODE_READ)
//            } catch (ex: java.io.FileNotFoundException) {
//                //failed to open the files, so close them now
//                IOUtil.close(otCompRaf)
//                IOUtil.close(otTextRaf)
//                IOUtil.close(otIdxRaf)
//
//                assert(false) { ex }
//                LOGGER.error("Could not open OT", ex)
//            }
//        }
//
//        // why do swallow the exception and log. Can Books have one testament
//        // without the other.
//        if (ntIdxFile.canRead()) {
//            try {
//                ntCompRaf = java.io.RandomAccessFile(ntIdxFile, FileUtil.MODE_READ)
//                ntTextRaf = java.io.RandomAccessFile(ntTextFile, FileUtil.MODE_READ)
//                ntIdxRaf = java.io.RandomAccessFile(ntCompFile, FileUtil.MODE_READ)
//            } catch (ex: java.io.FileNotFoundException) {
//                //failed to open the files, so close them now
//                IOUtil.close(ntCompRaf)
//                IOUtil.close(ntTextRaf)
//                IOUtil.close(ntIdxRaf)
//
//                assert(false) { ex }
//                LOGGER.error("Could not open OT", ex)
//            }
//        }
    }

    companion object {
        private const val SUFFIX_PART1 = "z"

        private const val SUFFIX_INDEX = "v"
        private const val SUFFIX_COMP = "s"
        private const val SUFFIX_TEXT = "z"

        /**
         * The log stream
         */
//        private val LOGGER: Logger = LoggerFactory.getLogger(ZVerseBackendState::class.java)
    }
}
