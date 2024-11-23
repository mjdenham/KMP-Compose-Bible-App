package com.martin.bibleapp.ui.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
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
    val pagerState = rememberPagerState(pageCount = { 2 })
    LaunchedEffect(pagerState) {
        // Collect from the a snapshotFlow reading the currentPage
        snapshotFlow { pagerState.currentPage }.collect { page ->
            // Do something with each page change, for example:
            viewModel.pageSelected(page)
        }
    }

    val documentState by viewModel.documentState.collectAsStateWithLifecycle()
    documentState.let { state ->
        when (state) {
            is ResultIs.Loading -> LoadingIndicator()
            is ResultIs.Error -> ErrorMessage()
            is ResultIs.Success -> {
                HorizontalPager(
                    state = pagerState,
                    beyondViewportPageCount = 1,
                ) { page ->
                    showBible(viewModel, state, page)
                }
            }
        }
    }
}

@Composable
private fun showBible(
    viewModel: DocumentViewModel,
    state: ResultIs.Success<DocumentModel>,
    currentPage: Int
) {
    println("ZZZZZ showBible called for page $currentPage")
    val tabState = state.data.tabStateList[currentPage]
    val html = viewModel.updateTextColour(tabState.html, getTextColour())
    if (currentPage == 0) {
        ShowHtmlBSB(html, tabState.verse.getOsisID()) {
            viewModel.updateVerse(it)
        }
    } else {
        ShowHtmlCommentary(html, tabState.verse.getOsisID())
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun getTextColour() = "#"+MaterialTheme.colorScheme.primary.toArgb().toHexString(HexFormat.Default).takeLast(6)

@Composable
private fun ShowHtmlBSB(html: String, reference: String, updateVerse: (String) -> Unit) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = html,
        baseUrl = "http://bible#$reference"
    )
    val webViewNavigator = rememberWebViewNavigator()

    val jsBridge = rememberWebViewJsBridge().apply {
        register(BibleJsMessageHandler(updateVerse))
    }

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        webViewJsBridge = jsBridge,
        modifier = Modifier.fillMaxSize(),
        onCreated = { webView ->
            println("ZZZZZZ onCreated page: 0")
        },
        onDispose = { webView ->
            println("ZZZZZZ onDispose webView 0")
        }
    )
}

@Composable
private fun ShowHtmlCommentary(html: String, reference: String) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = html,
        baseUrl = "http://bible#$reference"
    )
    val webViewNavigator = rememberWebViewNavigator()

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        modifier = Modifier.fillMaxSize(),
        onCreated = { webView ->
            println("ZZZZZZ onCreated page: 1")
        },
        onDispose = { webView ->
            println("ZZZZZZ onDispose webView 1")
        }
    )
}