@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package com.martin.bibleapp.ui.selector

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference

@Composable
fun ShowSelector(
    viewModel: SelectorViewModel = viewModel { SelectorViewModel() },
    modifier: Modifier = Modifier,
    onSelected: (Reference) -> Unit) {
    val documentState by viewModel.selectorState.collectAsState()
    val book = documentState.book
    if (book == null) {
        BookSelectionScreen({ viewModel.selectBook(it) }, modifier)
    } else {
        ChapterSelectionScreen(documentState.numChapters!!, modifier) { chap ->
            onSelected(Reference(book, chap))
        }
    }
}

@Composable
private fun BookSelectionScreen(
    onSelected: (BibleBook) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(getColumnCount()),
    ) {
        items(BibleBook.entries) {
            SelectionButton(it.shortLabel(), modifier) {
                onSelected(it)
            }
        }
    }
}

@Composable
fun ChapterSelectionScreen(
    numChapters: Int,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(getColumnCount())) {
        items(( 1..numChapters).toList()) {
            SelectionButton(it.toString(), modifier) {
                onSelected(it)
            }
        }
    }
}

@Composable
private fun SelectionButton(
    text: String,
    modifier: Modifier,
    onSelected: () -> Unit
) {
    OutlinedButton(
        onClick = { onSelected() },
        modifier = modifier.padding(4.dp, 0.dp),
        shape = RoundedCornerShape(20),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = modifier.padding(0.dp)
        )
    }
}

@Composable
private fun getColumnCount(): Int =
    if (calculateWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Expanded) {
        13
    } else {
        6
    }