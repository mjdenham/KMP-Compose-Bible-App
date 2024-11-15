package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.osisconverter.OsisToHtml
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange

class ReadPageUseCase(private val reader: BibleReader) {
    suspend fun readPage(verse: Verse): String {
        val v11n = verse.getVersification()
        val start = Verse(v11n, verse.book, verse.chapter, 1)
        val end =
            Verse(v11n, verse.book, verse.chapter, v11n.getLastVerse(verse.book, verse.chapter))

        val osisList = reader.getOsisList(VerseRange(v11n, start, end))

        return OsisToHtml().convertToHtml(osisList)
    }
}