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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.ui.util.OrientationProvider
import com.martin.bibleapp.ui.util.OrientationProviderImpl

@Composable
fun BookSelectionScreen(
    modifier: Modifier = Modifier,
    orientation: OrientationProvider.Orientation = OrientationProviderImpl().getOrientation(),
    onSelected: (BibleBook) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(getColumnCount(orientation)),
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
    book: BibleBook,
    modifier: Modifier = Modifier,
    viewModel: ChapterSelectorViewModel = viewModel { ChapterSelectorViewModel(book) },
    orientation: OrientationProvider.Orientation = OrientationProviderImpl().getOrientation(),
    onSelected: (Int) -> Unit
) {
    val documentState: SelectionModel by viewModel.selectorState.collectAsState()

    documentState.numChapters?.let { chaps ->
        LazyVerticalGrid(columns = GridCells.Fixed(getColumnCount(orientation))) {
            items((1..chaps).toList()) {
                SelectionButton(it.toString(), modifier) {
                    onSelected(it)
                }
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
private fun getColumnCount(orientation: OrientationProvider.Orientation): Int =
    if (orientation == OrientationProvider.Orientation.Landscape) {
        ColumnsLandscape
    } else {
        ColumnsPortrait
    }

private const val ColumnsLandscape = 13
private const val ColumnsPortrait = 6