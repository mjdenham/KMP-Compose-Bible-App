package com.martin.bibleapp.ui.appsetup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppSetup(
    modifier: Modifier = Modifier,
    appSetupViewModel: AppSetupViewModel = koinViewModel()
) {
    appSetupViewModel.setupApp {
        println("App setup complete")
    }
}