package com.martin.bibleapp.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.martin.bibleapp.resources.Res
import com.martin.bibleapp.resources.error
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoadingIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(80.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
    }
}

@Composable
fun ErrorMessage() {
    Text(
        text = stringResource(Res.string.error),
        modifier = Modifier.padding(15.dp)
    )
}