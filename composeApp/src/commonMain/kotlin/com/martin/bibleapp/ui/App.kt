package com.martin.bibleapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.ui.document.Document
import com.martin.bibleapp.ui.search.SearchScreen
import com.martin.bibleapp.ui.selector.BookSelectionScreen
import com.martin.bibleapp.ui.selector.ChapterSelectionScreen
import com.martin.bibleapp.ui.theme.BibleTheme
import com.martin.bibleapp.ui.document.DocumentTopNavBar
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
            Scaffold(
                topBar = {
                    DocumentTopNavBar(navController)
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
                        Document()
                    }
                    composable<BibleScreen.BibleBookPicker> {
                        BookSelectionScreen(onSelected = { book ->
                            navController.navigate(BibleScreen.BibleChapterPicker(book.name))
                        })
                    }
                    composable<BibleScreen.BibleChapterPicker> {
                        val bookName = it.toRoute<BibleScreen.BibleChapterPicker>().bookName
                        val book = BibleBook.valueOf(bookName)
                        ChapterSelectionScreen(book) { selectedChapter ->
                            navController.popBackStack(BibleScreen.BibleView, false)
                        }
                    }
                    composable<BibleScreen.Search> {
                        SearchScreen()
                    }
                }
            }
        }
    }
}
