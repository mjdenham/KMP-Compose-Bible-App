package com.martin.bibleapp.domain.bible

object TabDocuments {
    enum class Document(val osisName: String, val pageType: PageType, val changeVerseOnScroll: Boolean) {
        BSB("BSB",PageType.CHAPTER,true),
        KING_COMMENTS("KingComments", PageType.VERSE,false)
    }

    enum class PageType {
        CHAPTER,
        VERSE
    }

    val tabs = mapOf(0 to Document.BSB, 1 to Document.KING_COMMENTS)
}