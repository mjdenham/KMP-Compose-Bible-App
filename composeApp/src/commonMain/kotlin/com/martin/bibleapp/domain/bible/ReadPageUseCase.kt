package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.osisconverter.OsisToHtml
import com.martin.bibleapp.domain.reference.Reference
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.system.Versifications

class ReadPageUseCase(private val reader: BibleReader) {
    suspend fun readPage(reference: Reference): String {
        val v11n = Versifications.instance().getVersification(Versifications.DEFAULT_V11N)
        val start = Verse(v11n, reference.book, reference.chapter, 1)
        val end = Verse(v11n, reference.book, reference.chapter, v11n.getLastVerse(reference.book, reference.chapter))

        val osisList = reader.getOsisList(VerseRange(v11n, start, end))

        val html = OsisToHtml().convertToHtml(osisList)

        return html
    }
}