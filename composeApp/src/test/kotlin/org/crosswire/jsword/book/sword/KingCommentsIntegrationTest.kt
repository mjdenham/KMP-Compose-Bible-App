package org.crosswire.jsword.book.sword

import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.Path.Companion.toPath
import org.crosswire.common.util.IoUtil
import org.crosswire.common.util.WebResource
import org.crosswire.common.util.delete
import org.crosswire.jsword.book.sword.state.ZVerseBackendState
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.system.Versifications
import org.junit.AfterClass
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertContains
import kotlin.time.measureTime

class KingCommentsIntegrationTest {

    private val webResource = WebResource()
    private val ioUtil = IoUtil()

    private val bookMetaData = SwordBookMetaData().apply {
        library = folderToUnzipInto.toString()
        setProperty(SwordBookMetaData.KEY_DATA_PATH, "./modules/comments/zcom/kingcomments/")
    }
    private lateinit var backendState: ZVerseBackendState
    private lateinit var backend: ZVerseBackend

    companion object {
        @JvmStatic
        @AfterClass
        @BeforeClass
        fun removeDownloads(): Unit {
            zipFilePath.delete()
            folderToUnzipInto.delete()
        }

        const val downloadUrl = "https://www.crosswire.org/ftpmirror/pub/sword/packages/rawzip/KingComments.zip"
        val zipFilePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("downloaded.zip".toPath())
        val folderToUnzipInto = FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("unzipto".toPath())
    }

    @Test
    fun readRawContent_readFirstVerse() {
        testDownloaded()
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val result = backend.readRawContent(backendState, Verse(v11n, BibleBook.GEN, 1, 1))
        println(result)
        assertContains(
            result,
            "Reading and studying God's Word gave me a deep desire to preach and share God’s Word both in teaching the believers to the edification of the church of God, with an especial care for young believers"
        )
    }


    @Test
    fun readRawContent_readLastVerseInChapter() {
        testDownloaded()
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val result = backend.readRawContent(backendState, Verse(v11n, BibleBook.GEN, 1, 31))
        assertContains(
            result,
            "The sixth day is a unique day."
        )
    }

    @Test
    fun readRawContent_readNtVerse() {
        testDownloaded()
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val result = backend.readRawContent(backendState, Verse(v11n, BibleBook.JOHN, 1, 1))
        println(result)
        assertContains(
            result,
            "The purpose of this Gospel is to look at the Lord Jesus as God the Son."
        )
    }

    @Test
    fun readToOsis_readChapter() {
        testDownloaded()
        val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
        val v11n = Versifications.instance().getVersification(v11nName)
        val start = Verse(v11n, BibleBook.GEN, 1, 1)
        val end = Verse(v11n, BibleBook.GEN, 1, 31)
        val result: List<KeyText>
        val time = measureTime {
            result = backend.readToOsis(VerseRange(v11n, start, end))
        }
        println("Time taken: ${time.inWholeMilliseconds} ms")
        assertContains(
            result[1].text,
            "Then we see that God continues to work. His Spirit"
        )
        assertContains(
            result.last().text,
            "The sixth day is a unique day."
        )
    }

    fun testDownloaded() = runTest {
        if (!FileSystem.SYSTEM.exists(folderToUnzipInto.resolve("mods.d/kingcomments.conf"))) {
            println("Temp dir: " + FileSystem.SYSTEM_TEMPORARY_DIRECTORY)
            zipFilePath.delete()

            val success = webResource.download(downloadUrl, zipFilePath)
            assertTrue(success)
            assertTrue(FileSystem.SYSTEM.exists(zipFilePath))

            ioUtil.unpackZip(
                zipFilePath,
                folderToUnzipInto,
                true,
                SwordConstants.DIR_CONF,
                SwordConstants.DIR_DATA
            )
            assertTrue(FileSystem.SYSTEM.exists(folderToUnzipInto.resolve("mods.d/kingcomments.conf")))
        }

        backendState = ZVerseBackendState(bookMetaData, BlockType.BLOCK_BOOK)
        backend = ZVerseBackend(bookMetaData, BlockType.BLOCK_BOOK, 2)
    }
}