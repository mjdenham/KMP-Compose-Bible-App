package com.martin.bibleapp.ui.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
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
            is ResultIs.Success -> {
                val html = viewModel.updateTextColour(state.data.htmlText, getTextColour())
                ShowHtml(html, state.data.verse.getOsisID()) {
                    viewModel.updateVerse(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun getTextColour() = "#"+MaterialTheme.colorScheme.primary.toArgb().toHexString(HexFormat.Default).takeLast(6)

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