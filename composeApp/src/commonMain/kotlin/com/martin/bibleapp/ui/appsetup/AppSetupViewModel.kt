package com.martin.bibleapp.ui.appsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin.bibleapp.domain.install.InstallBsbUseCase
import kotlinx.coroutines.launch

class AppSetupViewModel(private val installBsbUseCase: InstallBsbUseCase): ViewModel() {

    fun setupApp(afterSetup: () -> Unit) {
        viewModelScope.launch {
            installBsbUseCase.installBsbModule()
            afterSetup()
        }
    }

    companion object {
        private const val HEAD_STYLE = "<head><style>body { font-size: 16pt; line-height: 1.8; margin: 0px 20px 0px 20px } p { padding:0px; margin:0px; }</style></head>"
    }
}