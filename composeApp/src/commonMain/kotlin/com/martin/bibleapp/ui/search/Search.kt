package com.martin.bibleapp.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import com.martin.bibleapp.domain.reference.VerseText
import com.martin.bibleapp.ui.util.ErrorMessage
import com.martin.bibleapp.ui.util.LoadingIndicator
import com.martin.bibleapp.ui.util.ResultIs
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel()
) {
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(true) }
    val suggestions by viewModel.suggestions.collectAsState()
    val searchResults by viewModel.searchResultsState.collectAsState()

    Column(Modifier.fillMaxSize().semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = modifier
                .fillMaxWidth()
                .semantics { traversalIndex = 0f },
            query = text,
            onQueryChange = { text = it },
            onSearch = {
                expanded = false
                viewModel.search(text)
            },
            placeholder = { Text("Hinted search text") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            active = expanded,
            onActiveChange = { expanded = it },
        ) {
            LazyColumn {
                items(suggestions) {
                    ListItem(
                        headlineContent = { Text(it) },
                        leadingContent = { Icon(Icons.Default.Refresh, contentDescription = null) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clickable {
                                text = it
                                expanded = false
                                viewModel.search(text)
                            }
                    )
                }
            }
        }

        if (!expanded) {
            when (val result = searchResults) {
                is ResultIs.Success -> SearchResults(modifier, result.data)
                is ResultIs.Loading -> LoadingIndicator()
                is ResultIs.Error -> ErrorMessage()
            }
        }
    }
}

@Composable
private fun SearchResults(
    modifier: Modifier,
    results: List<VerseText>
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.semantics { traversalIndex = 1f },
    ) {

        items(results) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                ItemCard(it)
            }
        }
    }
}

@Composable
private fun ItemCard(it: VerseText) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.padding(10.dp),
        ) {
            Text(
                text = it.reference.shortLabel(),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = it.text,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}