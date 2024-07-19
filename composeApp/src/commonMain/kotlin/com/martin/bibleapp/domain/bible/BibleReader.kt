package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.reference.BibleBook

interface BibleReader {
    suspend fun read(book: BibleBook): String
    suspend fun countChapters(book: BibleBook): Int
}