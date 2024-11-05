package com.martin.bibleapp.ui.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.martin.bibleapp.ui.util.ErrorMessage
import com.martin.bibleapp.ui.util.LoadingIndicator
import com.martin.bibleapp.ui.util.ResultIs
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Document(
    viewModel: DocumentViewModel = koinViewModel()
) {
    val documentState by viewModel.documentState.collectAsStateWithLifecycle()
    documentState.let { state ->
        when (state) {
            is ResultIs.Loading -> LoadingIndicator()
            is ResultIs.Error -> ErrorMessage()
            is ResultIs.Success -> ShowHtml(state.data.htmlText, state.data.reference.referenceCode()) {
                viewModel.updateVerse(it)
            }
        }
    }
}

@Composable
private fun ShowHtml(html: String, reference: String, updateVerse: (String) -> Unit) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = html,
        baseUrl = "http://bible#$reference"
    )
    val webViewNavigator = rememberWebViewNavigator()

    val jsBridge = rememberWebViewJsBridge()
    LaunchedEffect(jsBridge) {
        jsBridge.register(BibleJsMessageHandler(updateVerse))
    }

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        webViewJsBridge = jsBridge,
        modifier = Modifier.fillMaxSize()
    )
}