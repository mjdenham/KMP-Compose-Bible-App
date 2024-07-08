package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.data.BibleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentViewModel: ViewModel() {
    private val _documentState = MutableStateFlow<DocumentState>(DocumentState.Loading)
    val documentState: StateFlow<DocumentState> = _documentState

    private var showGenesis = true

    init {
        showPassage()
    }

    private fun showPassage(book: String = "GEN") {
        viewModelScope.launch {
            val page = BibleData().readPage(book)
            _documentState.value = DocumentState.Success(DocumentModel(book, "<html><body>$page</body></html>"))
        }
    }

    fun changeContent() {
        showGenesis = !showGenesis
        showPassage(if (showGenesis) "GEN" else "EXO")
    }
}