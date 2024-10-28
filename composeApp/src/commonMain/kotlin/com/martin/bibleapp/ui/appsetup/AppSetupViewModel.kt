package com.martin.bibleapp.ui.appsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.install.InstallBsbUseCase
import com.martin.bibleapp.ui.util.ResultIs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppSetupViewModel(private val installBsbUseCase: InstallBsbUseCase): ViewModel() {

    private val _setupResultsState = MutableStateFlow<ResultIs<Boolean>>(ResultIs.Loading)
    val setupResultsState = _setupResultsState.asStateFlow()

    init {
        setupApp()
    }

    private fun setupApp() {
        _setupResultsState.value = ResultIs.Loading
        viewModelScope.launch {
            installBsbUseCase.installBsbModule()
            _setupResultsState.value = ResultIs.Success(true)
        }
    }
}