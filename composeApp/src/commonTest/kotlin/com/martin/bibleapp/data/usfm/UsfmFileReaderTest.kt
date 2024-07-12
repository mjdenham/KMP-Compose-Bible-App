package com.martin.bibleapp.data.usfm

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class UsfmFileReaderTest {

    @Test
    fun shouldRemoveSlashF() {
        val expected = "And God said, “Let there be light,”  and there was light. "
        val html = UsfmFileReader().toHtml("\\v 3 And God said, “Let there be light,” \\f + \\fr 1:3 \\ft Cited in 2 Corinthians 4:6\\f* and there was light. ")
        assertContains(html, expected)
    }

    @Test
    fun handleVerse() {
        val expected = "<small>3</small> And God said, “Let there be light,”  and there was light. "
        val html = UsfmFileReader().toHtml("\\v 3 And God said, “Let there be light,” \\f + \\fr 1:3 \\ft Cited in 2 Corinthians 4:6\\f* and there was light. ")
        assertEquals(expected, html)
    }

    @Test
    fun handleHeading() {
        val expected = "<h2>Genesis</h2>"
        val html = UsfmFileReader().toHtml("\\h Genesis")
        assertEquals(expected, html)
    }

    @Test
    fun handleChapter() {
        val expected = "<h3>Chapter 1</h3>"
        val html = UsfmFileReader().toHtml("\\c 1")
        assertEquals(expected, html)
    }

    @Test
    fun handleIndentQn() {
        val expected = "<p>Blessed is the man</p>"
        val html = UsfmFileReader().toHtml("\\q1 Blessed is the man")
        assertEquals(expected, html)

        val expected2 = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;who does not walk in the counsel of the wicked,</p>"
        val html2 = UsfmFileReader().toHtml("\\q2 who does not walk in the counsel of the wicked,")
        assertEquals(expected2, html2)
    }

    @Test
    fun shouldHandleBTagAsBlankLine() {
        val expected = "<br />"
        val html = UsfmFileReader().toHtml("\\b")
        assertEquals(expected, html)
    }
}