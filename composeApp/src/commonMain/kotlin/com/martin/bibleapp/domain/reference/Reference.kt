package com.martin.bibleapp.domain.reference

data class Reference(val book: BibleBook, val chapter: Int = 1) {
    fun shortLabel() = "${book.shortLabel()} $chapter"
    fun referenceCode() = "$chapter.1"
}
