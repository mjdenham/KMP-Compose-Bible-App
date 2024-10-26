package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.repository.sword.SwordReader
import com.martin.bibleapp.fakes.FakeCurrentReferenceRepository
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import org.crosswire.jsword.book.sword.SwordBookPath
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BibleTest {

    private lateinit var bible: Bible

    @BeforeTest
    fun setup() {
        SwordBookPath.swordBookPath = "/Users/martin/StudioProjects/kmp-sword/testFiles/BSB/".toPath()
        bible = Bible(SwordReader(), FakeCurrentReferenceRepository())
    }

    @Test
    fun shouldFindAbrahamInGenesis() {
        runBlocking {
            val result = bible.search("Abraham")
            assertTrue(result.size > 30)
        }
    }

    @Test
    fun shouldFindAllWords() {
        runBlocking {
            val result = bible.search("Abraham Sarah")
            assertEquals(19, result.size)
        }
    }
}