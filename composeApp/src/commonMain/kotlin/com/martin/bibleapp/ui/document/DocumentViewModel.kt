package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.reference.BibleBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentViewModel: ViewModel() {
    private val _documentState = MutableStateFlow<DocumentState>(DocumentState.Loading())
    val documentState: StateFlow<DocumentState> = _documentState

    init {
        showPassage(BibleBook.GEN)
    }

    private fun showPassage(book: BibleBook) {
        viewModelScope.launch {
            val page = Bible().readPage(book)
            _documentState.value = DocumentState.Success(DocumentModel(book, "<html><body>$page</body></html>"))
        }
    }

    fun selectBook(book: BibleBook) {
        showPassage(book)
    }
}