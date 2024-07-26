package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference

class Bible(private val reader: BibleReader = UsfmFileReader()) {
    suspend fun readPage(reference: Reference): String {
        return reader.read(reference)
    }

    suspend fun getNumChapters(book: BibleBook): Int {
        return reader.countChapters(book)
    }
}