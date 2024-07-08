package com.martin.bibleapp.data.repository

import com.martin.bibleapp.domain.reference.BibleBook

class BibleData {
    suspend fun readPage(book: BibleBook): String {
        return UsfmFileReader().readFile(book)
    }
}