package com.martin.bibleapp.ui.document

import com.martin.bibleapp.domain.reference.BibleBook

data class DocumentModel(val reference: BibleBook, val htmlText: String)