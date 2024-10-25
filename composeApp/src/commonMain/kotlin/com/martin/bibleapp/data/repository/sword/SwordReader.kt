package com.martin.bibleapp.data.repository.sword

import com.martin.bibleapp.domain.bible.BibleReader
import org.crosswire.jsword.versification.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.domain.reference.VerseText
import org.crosswire.jsword.book.sword.BlockType
import org.crosswire.jsword.book.sword.SwordBookMetaData
import org.crosswire.jsword.book.sword.SwordBookPath
import org.crosswire.jsword.book.sword.ZVerseBackend
import org.crosswire.jsword.book.sword.state.ZVerseBackendState
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.system.Versifications
import kotlin.time.measureTime

class SwordReader: BibleReader {
    private val swordBookPath = SwordBookPath.getSwordBookPath()

    val v11nName = "KJV" //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
    private val bookMetaData = SwordBookMetaData().apply {
        library = swordBookPath.toString()
        setProperty(SwordBookMetaData.KEY_DATA_PATH, "./modules/texts/ztext/bsb/")
    }
    private var backend = ZVerseBackend(bookMetaData)

    override suspend fun read(reference: Reference): String {
        val v11n = Versifications.instance().getVersification(v11nName)
        val start = Verse(v11n, reference.book, reference.chapter, 1)
        val end = Verse(v11n, reference.book, reference.chapter, v11n.getLastVerse(reference.book, reference.chapter))
        val result: List<String>
        val time = measureTime {
            result = backend.readToOsis(VerseRange(v11n, start, end))
        }
        println("Time taken: ${time.inWholeMilliseconds} ms")

        println("Sword text: $result")

        return "<container>" + result.joinToString(" ")+"</container>"
    }

    override suspend fun countChapters(book: BibleBook): Int {
        val v11n = Versifications.instance().getVersification(v11nName)
        return v11n.getLastChapter(book)
    }

    override suspend fun getVersesPlainText(book: BibleBook): List<VerseText> {
        return listOf()
    }
}