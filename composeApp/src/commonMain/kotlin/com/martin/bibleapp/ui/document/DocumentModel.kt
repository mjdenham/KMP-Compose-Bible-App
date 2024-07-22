package com.martin.bibleapp.ui.document

import com.martin.bibleapp.domain.reference.Reference

data class DocumentModel(val reference: Reference, val htmlText: String)