package com.martin.bibleapp.domain.reference

import org.crosswire.jsword.versification.BibleBook

data class Reference(val book: BibleBook, val chapter: Int = 1, val verse: Int = 1) {
    fun shortLabel() = "${book.oSIS} $chapter:$verse"
    fun referenceCode() = toCode(book, chapter, verse)

    companion object {
        val DEFAULT = Reference(BibleBook.JOHN, 3, 16)

        fun toCode(book: BibleBook, chapter: Int, verse: Int): String = "${book.oSIS}.$chapter.$verse"
        fun toCode(book: BibleBook, chapter: Int): String = "${book.oSIS}.$chapter"
    }
}
