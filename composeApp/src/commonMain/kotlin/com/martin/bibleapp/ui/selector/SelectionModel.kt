package com.martin.bibleapp.ui.selector

import org.crosswire.jsword.versification.BibleBook

data class SelectionModel(val book: BibleBook? = null, val numChapters: Int? = null, val chapter: Int? = null)