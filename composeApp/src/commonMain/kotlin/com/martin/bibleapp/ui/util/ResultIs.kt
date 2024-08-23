package com.martin.bibleapp.ui.util

sealed interface ResultIs<out T> {

    data class Success<out T>(
        val data: T,
    ) : ResultIs<T>

    data class Error(
        val message: String,
        val throwable: Throwable? = null
    ) : ResultIs<Nothing>

    object Loading : ResultIs<Nothing>
}