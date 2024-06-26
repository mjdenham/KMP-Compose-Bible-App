package com.martin.bibleapp.ui.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

@Composable
fun showDocument(html: String) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = html
    )
    val webViewNavigator = rememberWebViewNavigator()
    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        modifier = Modifier.fillMaxSize()
    )
}