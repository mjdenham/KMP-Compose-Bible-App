package com.martin.bibleapp.data.repository.sword

import com.martin.bibleapp.domain.bible.BibleReader
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.book.sword.SwordBookMetaData
import org.crosswire.jsword.book.sword.SwordBookPath
import org.crosswire.jsword.book.sword.ZVerseBackend
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.system.Versifications

class SwordReader: BibleReader {

    private val v11nName = Versifications.DEFAULT_V11N //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);
    private val bookMetaData = SwordBookMetaData().apply {
        library = SwordBookPath.swordBookPath.toString()
        setProperty(SwordBookMetaData.KEY_DATA_PATH, "./modules/texts/ztext/bsb/")
    }
    private var backend = ZVerseBackend(bookMetaData)

    override suspend fun getOsis(verseRange: VerseRange): String {
        val result: List<KeyText> = backend.readToOsis(verseRange)

        return wrapXml(result.map { it.text }.joinToString(""))
    }

    override suspend fun getOsisList(verseRange: VerseRange): List<KeyText> {
        return backend.readToOsis(verseRange)
            .map { keyOsis ->
                KeyText(keyOsis.key, wrapXml(keyOsis.text))
            }
    }

    override suspend fun countChapters(book: BibleBook): Int {
        val v11n = Versifications.instance().getVersification(v11nName)
        return v11n.getLastChapter(book)
    }

    /**
     * The backend processing often leaves multiple top-level tags but xml should only have one top-level tag.
     * This wrapping ensures that is the case.
     */
    private fun wrapXml(text: String): String = "<container>" +
            text +
            "</container>"
}