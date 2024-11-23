package com.martin.bibleapp.ui.selector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.ReferenceSelectionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.crosswire.jsword.passage.Verse
import org.crosswire.jsword.versification.BibleBook

class ChapterSelectorViewModel(
    val book: BibleBook,
    val referenceSelectionUseCase: ReferenceSelectionUseCase
): ViewModel() {
    fun selectReference(verse: Verse) {
        viewModelScope.launch {
            referenceSelectionUseCase.selectVerse(verse)
        }
    }

    private val _selectorState = MutableStateFlow(SelectionModel())
    val selectorState: StateFlow<SelectionModel> = _selectorState.asStateFlow()

    init {
        viewModelScope.launch {
            val numChapters = referenceSelectionUseCase.getNumChapters(book)
            _selectorState.value = selectorState.value.copy(book = book, numChapters = numChapters)
        }
    }
}