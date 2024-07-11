package com.martin.bibleapp.ui.document

sealed interface DocumentState {
    val title: String

    data class Success(
        val data: DocumentModel,
        override val title: String = data.reference.shortLabel()
    ) : DocumentState

    data class Loading(
        override val title: String = "Loading"
    ) : DocumentState

    data class Error(
        override val title: String = "Error"
    ) : DocumentState
}