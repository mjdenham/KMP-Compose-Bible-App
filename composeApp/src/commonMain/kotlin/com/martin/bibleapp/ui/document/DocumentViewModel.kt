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
            ResultIs.Success(DocumentModel(reference, "<html><head>${DocumentHtml.STYLE}</head><body>$page ${DocumentHtml.JAVA_SCRIPT}</body></html>"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )

}