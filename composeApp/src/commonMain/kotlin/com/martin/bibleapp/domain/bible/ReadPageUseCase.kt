package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.osisconverter.OsisToHtml
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import kotlin.math.max
import kotlin.math.min

class ReadPageUseCase(private val reader: BibleReader) {
    suspend fun readPage(document: TabDocuments.Document, verseRange: VerseRange): String {
        val osisList = reader.getOsisList(document.osisName, verseRange)

        return OsisToHtml().convertToHtml(osisList)
    }

    fun calculatePageVerseRange(document: TabDocuments.Document, verse: Verse): VerseRange {
        val v11n = verse.getVersification()
        return if (document.pageType == TabDocuments.PageType.CHAPTER) {
            val startChapter = max(verse.chapter - 1, 1)
            val endChapter = min(verse.chapter + 1, v11n.getLastChapter(verse.book))
            VerseRange(
                v11n,
                Verse(v11n, verse.book, startChapter, 1),
                Verse(v11n, verse.book, endChapter, v11n.getLastVerse(verse.book, endChapter))
            )
        } else {
            VerseRange(v11n, verse, verse)
        }
    }
}