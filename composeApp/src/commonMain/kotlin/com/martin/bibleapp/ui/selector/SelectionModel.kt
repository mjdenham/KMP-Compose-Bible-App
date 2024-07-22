package com.martin.bibleapp.ui.selector

import com.martin.bibleapp.domain.reference.BibleBook

data class SelectionModel(val book: BibleBook? = null, val numChapters: Int? = null, val chapter: Int? = null)