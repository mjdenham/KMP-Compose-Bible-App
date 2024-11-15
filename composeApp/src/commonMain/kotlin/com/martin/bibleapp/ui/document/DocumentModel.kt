package com.martin.bibleapp.ui.document

import org.crosswire.jsword.passage.Verse

data class DocumentModel(val verse: Verse, val htmlText: String)