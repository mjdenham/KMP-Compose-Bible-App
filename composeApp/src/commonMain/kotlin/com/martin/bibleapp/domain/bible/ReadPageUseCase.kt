package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.osisconverter.OsisToHtml
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange

class ReadPageUseCase(private val reader: BibleReader) {
    suspend fun readPage(document: TabDocuments.Document, verse: Verse): String {
        val v11n = verse.getVersification()
        val verseRange = if (document.pageType == TabDocuments.PageType.CHAPTER) {
            VerseRange(
                v11n,
                Verse(v11n, verse.book, verse.chapter, 1),
                Verse(v11n, verse.book, verse.chapter, v11n.getLastVerse(verse.book, verse.chapter))
            )
        } else {
            VerseRange(v11n, verse, verse)
        }

        val osisList = reader.getOsisList(document.osisName, verseRange)

        return OsisToHtml().convertToHtml(osisList)
    }
}