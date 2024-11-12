package com.martin.bibleapp.ui.selector

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.crosswire.jsword.versification.DivisionName
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_NT
import org.crosswire.jsword.versification.system.SystemDefault.BOOKS_OT

class BookSelectorViewModel(): ViewModel() {

    private val _selectorState = MutableStateFlow(listOf<SectionBooks>())
    val sectionBooksState: StateFlow<List<SectionBooks>> = _selectorState.asStateFlow()

    init {
        _selectorState.value = listOf(
            SectionBooks(DivisionName.OLD_TESTAMENT.getName(), BOOKS_OT),
            SectionBooks(DivisionName.NEW_TESTAMENT.getName(), BOOKS_NT)
        )
    }
}