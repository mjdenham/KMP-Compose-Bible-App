package com.martin.bibleapp.ui.Test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    viewModel: TestViewModel = koinViewModel()
) {
    val output by viewModel.output.collectAsStateWithLifecycle()

    Column {
        Button(onClick = { viewModel.testWriteFile() }) {
            Text("Write File")
        }

        LazyColumn {
            items(output) {
                Text(it)
            }
        }
    }

}