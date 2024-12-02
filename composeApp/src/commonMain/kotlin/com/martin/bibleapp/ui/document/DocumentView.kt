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
    page: Int,
    viewModel: DocumentViewModel = koinViewModel()
) {
    LaunchedEffect(page) {
        viewModel.pageSelected(page)
    }

    val documentState by viewModel.documentState.collectAsStateWithLifecycle()
    documentState.let { state ->
        when (state) {
            is ResultIs.Loading -> LoadingIndicator()
            is ResultIs.Error -> ErrorMessage()
            is ResultIs.Success -> {
                showDocument(viewModel, state, page)
            }
        }
    }
}

@Composable
private fun showDocument(
    viewModel: DocumentViewModel,
    state: ResultIs.Success<DocumentModel>,
    currentPage: Int
) {
    val tabState = state.data.tabConfigList[currentPage]
    val html = viewModel.updateTextColour(tabState.html, getTextColour())
    if (currentPage == 0) {
        ShowHtml(html, tabState.verse.getOsisID()) {
            viewModel.updateVerse(it)
        }
    } else {
        ShowHtml(html, tabState.verse.getOsisID())
    }
}

@Composable
private fun ShowHtml(html: String, reference: String, updateVerse: ((String) -> Unit)? = null) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = html,
        baseUrl = "http://bible#$reference"
    )
    val webViewNavigator = rememberWebViewNavigator()

    val jsBridge = updateVerse?.let {
        rememberWebViewJsBridge().apply {
            register(BibleJsMessageHandler(updateVerse))
        }
    }

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        webViewJsBridge = jsBridge,
        modifier = Modifier.fillMaxSize(),
    )
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun getTextColour() = "#"+MaterialTheme.colorScheme.primary.toArgb().toHexString(HexFormat.Default).takeLast(6)