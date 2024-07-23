package com.martin.bibleapp.ui.selector

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
fun showSelector(
    viewModel: SelectorViewModel = viewModel { SelectorViewModel() },
    modifier: Modifier = Modifier,
    onSelected: (Reference) -> Unit) {
    val documentState by viewModel.selectorState.collectAsState()
    val book = documentState.book
    if (book == null) {
        bookSelectionScreen({ viewModel.selectBook(it) }, modifier)
    } else {
        chapterSelectionScreen(documentState.numChapters!!, modifier) { chap ->
            onSelected(Reference(book, chap))
        }
    }
}

@Composable
private fun bookSelectionScreen(
    onSelected: (BibleBook) -> Unit,
    modifier: Modifier
) {
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(BibleBook.entries) {
            OutlinedButton(
                onClick = { onSelected(it) },
                modifier = modifier.padding(4.dp, 0.dp)
            ) {
                Text(
                    text = it.shortLabel(),
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
    }
}

@Composable
fun chapterSelectionScreen(
    numChapters: Int,
    modifier: Modifier = Modifier,
    onSelected: (Int) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(( 1..numChapters).toList()) {
            OutlinedButton(
                onClick = { onSelected(it) },
                modifier = modifier.padding(4.dp, 0.dp)
            ) {
                Text(
                    text = it.toString(),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}