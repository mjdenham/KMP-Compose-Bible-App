package com.martin.bibleapp.data.repository.sword

import com.martin.bibleapp.domain.bible.BibleReader
import org.crosswire.jsword.book.Book
import org.crosswire.jsword.book.sword.BookType
import org.crosswire.jsword.book.sword.SwordBookMetaData
import org.crosswire.jsword.book.sword.SwordBookPath
import org.crosswire.jsword.passage.KeyText
import org.crosswire.jsword.passage.VerseRange
import org.crosswire.jsword.versification.BibleBook
import org.crosswire.jsword.versification.system.Versifications

class SwordReader: BibleReader {

    private val v11nName = Versifications.DEFAULT_V11N //getBookMetaData().getProperty(BookMetaData.KEY_VERSIFICATION);

    private val bsbBookMetaData = SwordBookMetaData().apply {
        library = SwordBookPath.swordBookPath.toString()
        setProperty(SwordBookMetaData.KEY_DATA_PATH, "./modules/texts/ztext/bsb/")
    }
    private val bsbBook: Book = BookType.Z_TEXT.getBook(bsbBookMetaData)

    private val kingCommentsBookMetaData = SwordBookMetaData().apply {
        library = SwordBookPath.swordBookPath.toString()
        setProperty(SwordBookMetaData.KEY_DATA_PATH, "./modules/comments/zcom/kingcomments/")
    }
    private val kingCommentsBook: Book = BookType.Z_TEXT.getBook(kingCommentsBookMetaData)
    private val bookByDocumentName = mapOf<String, Book>("BSB" to bsbBook, "KingComments" to kingCommentsBook)

    override suspend fun getOsisList(documentName: String, verseRange: VerseRange): List<KeyText> {
        return bookByDocumentName[documentName]?.let { book ->
            book.readToOsis(verseRange)
                .map { keyOsis ->
                    KeyText(keyOsis.key, wrapXml(keyOsis.text))
                }
        } ?: throw Exception("No book for document name $documentName")
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