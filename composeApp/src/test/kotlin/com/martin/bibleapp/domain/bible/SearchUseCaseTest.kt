package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.repository.sword.SwordReader
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import org.crosswire.jsword.book.sword.SwordBookPath
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchUseCaseTest {

    private lateinit var searchUseCase: SearchUseCase

    @BeforeTest
    fun setup() {
        SwordBookPath.swordBookPath = "../testFiles/BSB/".toPath()
        searchUseCase = SearchUseCase(SwordReader())
    }

    @Test
    fun shouldFindAbrahamInGenesis() {
        runBlocking {
            val result = searchUseCase.search("Abraham")
            assertTrue(result.size > 30)
        }
    }

    @Test
    fun shouldFindAllWords() {
        runBlocking {
            val result = searchUseCase.search("Abraham Sarah")
            assertEquals(19, result.size)
        }
    }
}