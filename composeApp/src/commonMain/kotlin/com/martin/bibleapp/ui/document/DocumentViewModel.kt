package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DocumentViewModel(val bible: Bible): ViewModel() {
    private val _documentState = MutableStateFlow<ResultIs<DocumentModel>>(ResultIs.Loading)
    val documentState = _documentState.asStateFlow()

    private fun showPassage(reference: Reference) {
        viewModelScope.launch {
            val page = bible.readPage(reference)
            _documentState.value = ResultIs.Success(DocumentModel(reference, "<html>$HEAD_STYLE<body>$page</body></html>"))
        }
    }

    fun selectReference(reference: Reference) {
        showPassage(reference)
    }

    companion object {
        private const val HEAD_STYLE = "<head><style>body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px } p { padding:0px; margin:0px; }</style></head>"
    }
}