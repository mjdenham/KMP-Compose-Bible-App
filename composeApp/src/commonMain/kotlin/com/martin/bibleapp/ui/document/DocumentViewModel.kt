package com.martin.bibleapp.ui.document

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.Bible
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class DocumentViewModel(val bible: Bible): ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val documentState = bible.getCurrentReferenceFlow()
        .mapLatest { reference ->
            val page = bible.readPage(reference)
            ResultIs.Success(DocumentModel(reference, "<html>$HEAD_STYLE<body>$page</body></html>"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )

    companion object {
        private const val HEAD_STYLE = "<head><style>body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px } p { padding:0px; margin:0px; text-indent: 5% } span.verse-no { font-size: 0.6em; font-weight: 300 }</style></head>"
    }
}