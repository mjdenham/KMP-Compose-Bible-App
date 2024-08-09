package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.domain.reference.VerseText

interface BibleReader {
    suspend fun read(reference: Reference): String
    suspend fun countChapters(book: BibleBook): Int
    suspend fun getVersesPlainText(book: BibleBook): List<VerseText>
}