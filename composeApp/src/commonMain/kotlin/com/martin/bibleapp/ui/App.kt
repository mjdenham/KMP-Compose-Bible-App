package com.martin.bibleapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.martin.bibleapp.ui.document.DocumentState
import com.martin.bibleapp.ui.document.DocumentViewModel
import com.martin.bibleapp.ui.document.showDocument
import com.martin.bibleapp.ui.selector.showBookSelector
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Values that represent the screens in the app
 */
@Serializable
object BibleView
@Serializable
object BibleBookPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    viewModel: DocumentViewModel = viewModel(),
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
                        (documentState as? DocumentState.Success)?.data?.let {
                            Text(it.reference.name.lowercase().capitalize(Locale.current))
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(BibleBookPicker) }) {
                            Icon(
                                imageVector = Icons.Filled.Place,
                                contentDescription = "Next book"
                            )
                        }
                    },
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BibleView,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable<BibleView> {
                    showDocument(documentState)
                }
                composable<BibleBookPicker> {
                    showBookSelector { book ->
                        viewModel.selectBook(book)
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}

