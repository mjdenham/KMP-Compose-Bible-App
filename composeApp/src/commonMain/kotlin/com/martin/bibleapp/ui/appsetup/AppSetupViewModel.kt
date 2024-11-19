package com.martin.bibleapp.ui.appsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.install.InstallDocumentsUseCase
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppSetupViewModel(private val installDocumentsUseCase: InstallDocumentsUseCase): ViewModel() {

    private val _setupResultsState = MutableStateFlow<ResultIs<Boolean>>(ResultIs.Loading)
    val setupResultsState = _setupResultsState.asStateFlow()

    init {
        setupApp()
    }

    private fun setupApp() {
        _setupResultsState.value = ResultIs.Loading
        viewModelScope.launch {
            installDocumentsUseCase.installDocuments()
            _setupResultsState.value = ResultIs.Success(true)
        }
    }
}