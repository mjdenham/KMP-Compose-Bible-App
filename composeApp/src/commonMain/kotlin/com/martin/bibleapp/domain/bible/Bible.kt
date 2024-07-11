package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.reference.BibleBook

class Bible(private val reader: BibleReader = UsfmFileReader()) {
    suspend fun readPage(book: BibleBook): String {
        return reader.read(book)
    }
}