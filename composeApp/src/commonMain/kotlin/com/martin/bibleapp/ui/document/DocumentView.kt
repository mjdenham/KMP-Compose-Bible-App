package com.martin.bibleapp.ui.document

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.martin.bibleapp.domain.reference.Reference
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

@Composable
fun showDocument(
    gotoReference: Reference,
    viewModel: DocumentViewModel = viewModel { DocumentViewModel() }
) {
    viewModel.selectReference(gotoReference)
    val documentState by viewModel.documentState.collectAsState()
    documentState.let { state ->
        val html: String
        val jumpTo: String
        when (state) {
            is DocumentState.Loading -> {
                html = "Loading..."
                jumpTo = ""
            }
            is DocumentState.Error -> {
                html = "Error"
                jumpTo = ""
            }
            is DocumentState.Success -> {
                html = state.data.htmlText
                jumpTo = state.data.reference.referenceCode()
            }
        }

        showHtml(html, jumpTo)
    }
}

@Composable
private fun showHtml(html: String, reference: String) {
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