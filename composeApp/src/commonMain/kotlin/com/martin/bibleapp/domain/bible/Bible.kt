package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.osisconverter.OsisToHtml
import com.martin.bibleapp.domain.osisconverter.OsisToText
import org.crosswire.jsword.versification.BibleBook
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.domain.reference.Reference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_NT
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_OT
import org.crosswire.jsword.versification.system.Versifications

class Bible(private val reader: BibleReader, private val currentReferenceRepository: CurrentReferenceRepository) {

    fun getCurrentReferenceFlow(): Flow<Reference> = currentReferenceRepository.getCurrentReferenceFlow()

    suspend fun readPage(reference: Reference): String {
        val v11n = Versifications.instance().getVersification(Versifications.DEFAULT_V11N)
        val start = Verse(v11n, reference.book, reference.chapter, 1)
        val end = Verse(v11n, reference.book, reference.chapter, v11n.getLastVerse(reference.book, reference.chapter))

        val osis = reader.getOsis(VerseRange(v11n, start, end))

        val html = OsisToHtml().convertToHtml(osis)

        return html
    }

    suspend fun search(searchText: String): List<KeyText> = withContext(Dispatchers.Default) {
        val searchWords = searchText.split(" ").map { "\\b$it\\b".toRegex(RegexOption.IGNORE_CASE) }

        val osisToText = OsisToText()

        getBibleBooks().map { bibleBook ->
            async {
                val v11n = Versifications.instance().getVersification(Versifications.DEFAULT_V11N)
                val start = Verse(v11n, bibleBook, 1, 1)
                val lastChapter = v11n.getLastChapter(bibleBook)
                val end = Verse(v11n, bibleBook, lastChapter, v11n.getLastVerse(bibleBook, lastChapter))

                reader.getOsisList(VerseRange(v11n, start, end))
                    .map { keyOsis -> KeyText(
                        keyOsis.key,
                        osisToText.convertToText(keyOsis.text)
                    )}
                    .filter { keyOsis ->
                        searchWords.all {
                            keyOsis.text.contains(it)
                        }
                    }
            }
        }
            .awaitAll()
            .flatten()
    }

    fun getBibleBooks(): List<BibleBook> = (BOOKS_OT + BOOKS_NT).toList()
}