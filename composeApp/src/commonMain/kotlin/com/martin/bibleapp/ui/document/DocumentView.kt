package com.martin.bibleapp.ui.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

@Composable
fun showDocument(documentState: DocumentState) {
    documentState.let { state ->
        val html: String = when (state) {
            is DocumentState.Loading -> "Loading..."
            is DocumentState.Error -> "Error"
            is DocumentState.Success -> state.data.htmlText
        }
        showHtml(html)
    }
}

@Composable
fun showHtml(html: String, reference: String = "1.1") {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = html,
        baseUrl = "http://bible#$reference"
    )
    val webViewNavigator = rememberWebViewNavigator()
    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        modifier = Modifier.fillMaxSize()
    )
}