package com.martin.bibleapp.domain.bible

import org.crosswire.jsword.versification.BibleBook
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.domain.reference.VerseText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Bible(private val reader: BibleReader, private val currentReferenceRepository: CurrentReferenceRepository) {

    fun getCurrentReferenceFlow(): Flow<Reference> = currentReferenceRepository.getCurrentReferenceFlow()

    suspend fun readPage(reference: Reference): String {
        return reader.read(reference)
    }

    suspend fun search(searchText: String): List<VerseText> = withContext(Dispatchers.Default) {
        val searchWords = searchText.split(" ").map { "\\b$it\\b".toRegex(RegexOption.IGNORE_CASE) }
        BibleBook.entries.map { bibleBook ->
            async {
                reader.getVersesPlainText(bibleBook)
                    .filter { verseText ->
                        searchWords.all {
                            verseText.text.contains(it)
                        }
                    }
            }
        }
            .awaitAll()
            .flatten()
    }
}