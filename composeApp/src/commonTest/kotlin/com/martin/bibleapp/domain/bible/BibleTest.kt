package com.martin.bibleapp.domain.bible

import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
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
            assertTrue(result.size > 10)
        }
    }
}