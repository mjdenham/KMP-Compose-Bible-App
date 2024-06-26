package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DocumentViewModel: ViewModel() {
    private val _documentState = MutableStateFlow<DocumentState>(DocumentState.Loading)
    val documentState: StateFlow<DocumentState> = _documentState

    init {
        loadDocument()
    }

    private fun loadDocument() {
//        Log.d(TAG, "Loading document")
        _documentState.value = DocumentState.Success(DocumentModel("<html><body>In the beginning was the Word, and the Word was with God, and the Word was God.</body></html>"))
    }

    fun changeContent() {
        _documentState.value = DocumentState.Success(DocumentModel("<html><body>In the beginning God created the heavens and the earth.</body></html>"))
    }
}