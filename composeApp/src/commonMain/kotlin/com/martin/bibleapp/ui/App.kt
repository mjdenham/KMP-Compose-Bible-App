package com.martin.bibleapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference
import com.martin.bibleapp.ui.document.Document
import com.martin.bibleapp.ui.search.SearchScreen
import com.martin.bibleapp.ui.selector.BookSelectionScreen
import com.martin.bibleapp.ui.selector.ChapterSelectionScreen
import com.martin.bibleapp.ui.theme.BibleTheme
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

/**
 * Values that represent the screens in the app
 */
sealed class BibleScreen {
    @Serializable
    object BibleView: BibleScreen()
    @Serializable
    object BibleBookPicker: BibleScreen()
    @Serializable
    data class BibleChapterPicker(val bookName: String): BibleScreen()
    @Serializable
    object Search: BibleScreen()
}

@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController()
) {
    BibleTheme {
        KoinContext {
            var gotoReference by remember { mutableStateOf(Reference.DEFAULT) }

            Scaffold(
                topBar = {
                    BibleTopNavBar(navController, gotoReference.shortLabel())
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = BibleScreen.BibleView,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    composable<BibleScreen.BibleView> {
                        Document(gotoReference)
                    }
                    composable<BibleScreen.BibleBookPicker> {
                        BookSelectionScreen(onSelected = { book ->
                            navController.navigate(BibleScreen.BibleChapterPicker(book.name))
                        })
                    }
                    composable<BibleScreen.BibleChapterPicker> {
                        val bookName = it.toRoute<BibleScreen.BibleChapterPicker>().bookName
                        val book = BibleBook.valueOf(bookName)
                        ChapterSelectionScreen(onSelected = { selectedChapter ->
                            gotoReference = Reference(book, selectedChapter)
                            navController.popBackStack(BibleScreen.BibleView, false)
                        })
                    }
                    composable<BibleScreen.Search> {
                        SearchScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BibleTopNavBar(
    navController: NavHostController,
    title: String
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            ElevatedButton(onClick = { navController.navigate(BibleScreen.BibleBookPicker) }) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(BibleScreen.Search) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            }
        }
    )
}

