package com.martin.bibleapp.domain.reference

data class Reference(val book: BibleBook, val chapter: Int = 1) {
    fun shortLabel() = "${book.shortLabel()} $chapter"
    fun referenceCode() = toCode(book, chapter)

    companion object {
        val DEFAULT = Reference(BibleBook.JOHN, 3)

        fun toCode(book: BibleBook, chapter: Int, verse: Int = 1): String = "${book.usfmCode}.$chapter.$verse"
    }
}
