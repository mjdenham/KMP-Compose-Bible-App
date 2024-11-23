package com.martin.bibleapp.ui.topnavbar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.bible.CurrentReferenceUseCase
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class DocumentTopNavBarViewModel(currentReferenceUseCase: CurrentReferenceUseCase): ViewModel()
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val verseState = currentReferenceUseCase.getCurrentReferenceFlow()
        .mapLatest { verse ->
            ResultIs.Success(verse)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResultIs.Loading,
        )
}