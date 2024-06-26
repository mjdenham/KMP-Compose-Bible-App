package com.martin.bibleapp.ui.document

sealed interface DocumentState {
    data class Success(val data: DocumentModel) : DocumentState
    data object Loading : DocumentState
    data object Error : DocumentState
}