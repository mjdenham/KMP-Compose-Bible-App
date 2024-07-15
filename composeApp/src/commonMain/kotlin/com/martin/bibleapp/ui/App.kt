package com.martin.bibleapp.ui

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.document.showDocument
import com.martin.bibleapp.ui.selector.showBookSelector
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Values that represent the screens in the app
 */
enum class BibleScreen {
    BibleView,
    BibleBookPicker
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    viewModel: DocumentViewModel = viewModel { DocumentViewModel() },
    navController: NavHostController = rememberNavController()
) {
    MaterialTheme {
        val documentState by viewModel.documentState.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        ElevatedButton(onClick = { navController.navigate(BibleScreen.BibleBookPicker.name) }) {
                            Text(
                                text = documentState.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    },
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BibleScreen.BibleView.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(BibleScreen.BibleView.name) {
                    showDocument(documentState)
                }
                composable(BibleScreen.BibleBookPicker.name) {
                    showBookSelector { book ->
                        viewModel.selectBook(book)
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

