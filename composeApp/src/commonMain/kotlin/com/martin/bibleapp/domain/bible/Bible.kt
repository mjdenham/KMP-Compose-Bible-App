package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.domain.reference.VerseText

class Bible(private val reader: BibleReader = UsfmFileReader()) {
    suspend fun readPage(reference: Reference): String {
        return reader.read(reference)
    }

    suspend fun getNumChapters(book: BibleBook): Int {
        return reader.countChapters(book)
    }

    suspend fun search(searchText:String): List<VerseText> {
        val searchWords = searchText.split(" ")
        return reader.getVersesPlainText(BibleBook.GEN)
            .filter { verseText -> searchWords.any { verseText.text.contains(it, true) } }
    }
}