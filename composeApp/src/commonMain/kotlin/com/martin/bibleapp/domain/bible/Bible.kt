package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.repository.usfm.UsfmFileReader
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.domain.reference.VerseText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class Bible(private val reader: BibleReader = UsfmFileReader()) {
    suspend fun readPage(reference: Reference): String {
        return reader.read(reference)
    }

    suspend fun getNumChapters(book: BibleBook): Int {
        return reader.countChapters(book)
    }

    suspend fun search(searchText: String): List<VerseText> = withContext(Dispatchers.Default) {
        val searchWords = searchText.split(" ")
        BibleBook.entries.map { bibleBook ->
            async {
                reader.getVersesPlainText(bibleBook)
                    .filter { verseText ->
                        searchWords.all {
                            verseText.text.contains(it, true)
                        }
                    }
            }
        }
            .awaitAll()
            .flatten()
    }
}