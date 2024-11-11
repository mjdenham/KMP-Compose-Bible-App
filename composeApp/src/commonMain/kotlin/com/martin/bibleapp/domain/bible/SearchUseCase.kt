package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.osisconverter.OsisToText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_NT
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_OT
import org.crosswire.jsword.versification.system.Versifications

class SearchUseCase(private val reader: BibleReader) {
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
                    )
                    }
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


    private fun getBibleBooks(): List<BibleBook> = (BOOKS_OT + BOOKS_NT).toList()
}