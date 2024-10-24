package com.martin.bibleapp.data.usfm

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import org.crosswire.jsword.versification.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.domain.reference.VerseText
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class UsfmFileReaderTest {

    private var reference = UsfmFileReader.CurrentReference(BibleBook.GEN)

    @BeforeTest
    fun setup() {
        reference = UsfmFileReader.CurrentReference(BibleBook.GEN)
    }

    @Test
    fun shouldFetchWholeChapter() {
        runBlocking {
            val text = UsfmFileReader().read(Reference(BibleBook.PS, 117))
            assertContains(text, "Chapter 117")
            assertFalse(text.contains("Chapter 116"))
            assertFalse(text.contains("Chapter 118"))
        }
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
        val expected = "<a id='GEN.1' /><h3>Chapter 1</h3>"
        val html = UsfmFileReader().toHtml("\\c 1", reference)
        assertEquals(expected, html)
    }

    @Test
    fun handleCentredParagraph() {
        val expected = "<div style='text-align:center'>THE KING OF THE JEWS. </div>"
        val html = UsfmFileReader().toHtml("\\pc THE KING OF THE JEWS. ", reference)
        assertEquals(expected, html)
    }

    @Test
    fun handleIndentQn() {
        val expected = "<p>Blessed is the man</p>"
        val html = UsfmFileReader().toHtml("\\q1 Blessed is the man", reference)
        assertEquals(expected, html)

        val expected2 = "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;who does not walk in the counsel of the wicked,</p>"
        val html2 = UsfmFileReader().toHtml("\\q2 who does not walk in the counsel of the wicked,", reference)
        assertEquals(expected2, html2)

        val expected3 = "<p style='text-align:right'>His loving devotion endures forever.</p>"
        val html3 = UsfmFileReader().toHtml("\\qr His loving devotion endures forever.", reference)
        assertEquals(expected3, html3)
    }

    @Test
    fun handleAcrostic() {
        val expected = "<p>ALEPH</p>"
        val html = UsfmFileReader().toHtml("\\qa ALEPH", reference)
        assertEquals(expected, html)
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

    @Test
    fun getVersesPlainText() {
        runBlocking {
            val result: List<VerseText> = UsfmFileReader().getVersesPlainText(BibleBook.JOHN)
            assertContains(result, VerseText(Reference(BibleBook.JOHN, 1, 1), "In the beginning was the Word, and the Word was with God, and the Word was God. "))
            assertContains(result, VerseText(Reference(BibleBook.JOHN, 19, 19), "Pilate also had a notice posted on the cross. It read: JESUS OF NAZARETH, THE KING OF THE JEWS. "))
        }
    }
}