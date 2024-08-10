package com.martin.bibleapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.reference.VerseText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val _searchResultsState = MutableStateFlow<List<VerseText>>(emptyList())
    val searchResultsState: StateFlow<List<VerseText>> = _searchResultsState

    fun search(searchText: String) {
        viewModelScope.launch {
            val searchResults = Bible().search(searchText)
            _searchResultsState.value = searchResults
        }
    }
}