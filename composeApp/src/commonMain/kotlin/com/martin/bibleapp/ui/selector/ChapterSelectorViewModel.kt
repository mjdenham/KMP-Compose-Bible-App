package com.martin.bibleapp.ui.selector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.reference.BibleBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChapterSelectorViewModel(val bible: Bible, private val savedStateHandle: SavedStateHandle): ViewModel() {
    private val _selectorState = MutableStateFlow<SelectionModel>(SelectionModel())
    val selectorState: StateFlow<SelectionModel> = _selectorState.asStateFlow()

    init {
        viewModelScope.launch {
            val book = savedStateHandle.get<String?>("bookName")?.let { bookName ->
                BibleBook.valueOf(bookName)
            } ?: BibleBook.GEN

            val numChapters = bible.getNumChapters(book)
            _selectorState.value = selectorState.value.copy(book = book, numChapters = numChapters)
        }
    }
}