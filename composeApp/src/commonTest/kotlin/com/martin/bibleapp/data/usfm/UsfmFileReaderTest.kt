package com.martin.bibleapp.data.usfm

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class UsfmFileReaderTest {

    @Test
    fun shouldRemoveSlashF() {
        runBlocking {
            val expected = "3 And God said, “Let there be light,”  and there was light. "
            val html = UsfmFileReader().toHtml("\\v 3 And God said, “Let there be light,” \\f + \\fr 1:3 \\ft Cited in 2 Corinthians 4:6\\f* and there was light. ")
            assertEquals(expected, html)
        }
    }
}