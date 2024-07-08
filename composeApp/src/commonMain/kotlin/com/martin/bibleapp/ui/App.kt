package com.martin.bibleapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.martin.bibleapp.ui.document.DocumentState
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.document.showDocument
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: DocumentViewModel = viewModel()
        val documentState by viewModel.documentState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        ElevatedButton(onClick = { viewModel.changeContent() }) {
                            (documentState as? DocumentState.Success)?.data?.let {
                                Text(it.reference)
                            }
                        }
                    },
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                documentState.let { state ->
                    val html: String = when(state) {
                        DocumentState.Loading -> "Loading..."
                        DocumentState.Error -> "Error"
                        is DocumentState.Success -> state.data.htmlText
                    }
                    showDocument(html)
                }

            }
        }
    }
}

