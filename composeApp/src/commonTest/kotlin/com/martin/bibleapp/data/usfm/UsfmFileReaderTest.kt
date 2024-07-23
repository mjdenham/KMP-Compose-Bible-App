package com.martin.bibleapp.data.usfm

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.reference.BibleBook
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UsfmFileReaderTest {

    private var reference = UsfmFileReader.CurrentReference(BibleBook.GEN)

    @BeforeTest
    fun setup() {
        reference = UsfmFileReader.CurrentReference(BibleBook.GEN)
    }

    @Test
    fun shouldRemoveSlashF() {
        val expected = "And God said, “Let there be light,”  and there was light. "
        val html = UsfmFileReader().toHtml(
            "\\v 3 And God said, “Let there be light,” \\f + \\fr 1:3 \\ft Cited in 2 Corinthians 4:6\\f* and there was light. ",
            reference
        )
        assertContains(html, expected)
    }

    @Test
    fun handleVerse() {
        val expected = "<a id='GEN.1.3' /><small>3</small> And God said, “Let there be light,”  and there was light. "
        val html = UsfmFileReader().toHtml(
            "\\v 3 And God said, “Let there be light,” \\f + \\fr 1:3 \\ft Cited in 2 Corinthians 4:6\\f* and there was light. ",
            reference
        )
        assertEquals(expected, html)
    }

    @Test
    fun handleHeading() {
        val expected = "<h2>Genesis</h2>"
        val html = UsfmFileReader().toHtml("\\h Genesis", reference)
        assertEquals(expected, html)
    }

    @Test
    fun handleChapter() {
        val expected = "<h3>Chapter 1</h3>"
        val html = UsfmFileReader().toHtml("\\c 1", reference)
        assertEquals(expected, html)
    }

    @Test
    fun handleIndentQn() {
        val expected = "<p>Blessed is the man</p>"
        val html = UsfmFileReader().toHtml("\\q1 Blessed is the man", reference)
        assertEquals(expected, html)

        val expected2 = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;who does not walk in the counsel of the wicked,</p>"
        val html2 = UsfmFileReader().toHtml(
            "\\q2 who does not walk in the counsel of the wicked,",
            reference
        )
        assertEquals(expected2, html2)
    }

    @Test
    fun shouldHandleBTagAsBlankLine() {
        val expected = "<br />"
        val html = UsfmFileReader().toHtml("\\b", reference)
        assertEquals(expected, html)
    }

    @Test
    fun countChaptersShouldCountCLines() {
        runBlocking {
            val count = UsfmFileReader().countChapters(BibleBook.GEN)
            assertEquals(50, count)
        }
    }

}