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

    init {
        loadDocument()
    }

    private fun loadDocument() {
        viewModelScope.launch {
            val page = BibleData().readPage()
            _documentState.value = DocumentState.Success(DocumentModel("<html><body>$page</body></html>"))
        }
    }

    fun changeContent() {
        _documentState.value = DocumentState.Success(DocumentModel("<html><body>In the beginning God created the heavens and the earth.</body></html>"))
    }
}