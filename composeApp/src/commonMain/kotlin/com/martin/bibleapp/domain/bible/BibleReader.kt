package com.martin.bibleapp.domain.bible

import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.VerseRange

interface BibleReader {
    suspend fun getOsisList(documentName: String, verseRange: VerseRange): List<KeyText>
    suspend fun countChapters(book: BibleBook): Int
}