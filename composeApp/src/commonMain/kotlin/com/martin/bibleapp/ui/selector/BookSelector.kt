package com.martin.bibleapp.ui.selector

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.martin.bibleapp.domain.reference.BibleBook

@Composable
fun showBookSelector(modifier: Modifier = Modifier, onSelected: (BibleBook) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(BibleBook.entries) {
            OutlinedButton(
                onClick = { onSelected(it) },
                modifier = modifier.padding(4.dp, 0.dp)
            ) {
                Text(it.name.lowercase().capitalize(Locale.current))
            }
        }
    }
}