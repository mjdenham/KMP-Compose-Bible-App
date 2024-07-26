package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.reference.Reference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentViewModel: ViewModel() {
    private val _documentState = MutableStateFlow<DocumentState>(DocumentState.Loading())
    val documentState: StateFlow<DocumentState> = _documentState

    private fun showPassage(reference: Reference) {
        viewModelScope.launch {
            val page = Bible().readPage(reference)
            _documentState.value = DocumentState.Success(DocumentModel(reference, "<html>$HEAD_STYLE<body>$page</body></html>"))
        }
    }

    fun selectReference(reference: Reference) {
        showPassage(reference)
    }

    companion object {
        private const val HEAD_STYLE = "<head><style>body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px }; p { padding:0px; margin:0px; }</style></head>"
    }
}