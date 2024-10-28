package com.martin.bibleapp.ui.appsetup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.martin.bibleapp.ui.util.ErrorMessage
import com.martin.bibleapp.ui.util.LoadingIndicator
import com.martin.bibleapp.ui.util.ResultIs
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppSetup(
    modifier: Modifier = Modifier,
    viewModel: AppSetupViewModel = koinViewModel(),
    onCompleted: () -> Unit
) {
    val setupResults by viewModel.setupResultsState.collectAsStateWithLifecycle()

    when (setupResults) {
        is ResultIs.Success -> {
            LaunchedEffect(Unit) {
                onCompleted()
            }
        }
        is ResultIs.Loading -> LoadingIndicator()
        is ResultIs.Error -> ErrorMessage()
    }
}