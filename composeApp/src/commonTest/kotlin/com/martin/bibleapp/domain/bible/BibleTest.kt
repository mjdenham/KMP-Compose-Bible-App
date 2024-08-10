package com.martin.bibleapp.domain.bible

import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BibleTest {

    private var bible = Bible()

    @BeforeTest
    fun setup() {
        bible = Bible()
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