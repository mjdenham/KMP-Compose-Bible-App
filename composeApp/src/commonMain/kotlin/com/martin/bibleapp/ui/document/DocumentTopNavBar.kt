package com.martin.bibleapp.ui.document

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.martin.bibleapp.ui.BibleScreen
import com.martin.bibleapp.ui.util.ResultIs
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentTopNavBar(
    navController: NavHostController,
    viewModel: DocumentViewModel = koinViewModel()
) {
    val verseState = viewModel.verseState.collectAsStateWithLifecycle()
    val title = when (val state = verseState.value) {
        is ResultIs.Loading -> "Loading..."
        is ResultIs.Error -> "Error"
        is ResultIs.Success -> state.data.getName()
    }
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
