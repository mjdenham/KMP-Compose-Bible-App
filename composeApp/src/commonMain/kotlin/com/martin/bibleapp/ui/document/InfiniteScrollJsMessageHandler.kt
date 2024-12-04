package com.martin.bibleapp.ui.document

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class InfiniteScrollJsMessageHandler(val getNextPage: ((String) -> Unit) -> Unit) : IJsMessageHandler {
    companion object {
        const val INFINITE_SCROLL_BRIDGE_METHOD = "INFINITE_SCROLL"
    }
    override fun methodName(): String = INFINITE_SCROLL_BRIDGE_METHOD

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        println("Infinite scroll handler called")
        getNextPage { page: String ->
            callback(escapeEcmaScript(page))
        }
    }

    private fun escapeEcmaScript(text: String): String {
        if (text.isEmpty()) return text

        return buildString(text.length + 100) {
            for (element in text) {
                when (element) {
                    '\'' -> append("\\'")
                    '\"' -> append("\\\"")
                    '\\' -> append("\\\\")
                    '/' -> append("\\/")
                    else -> append(element)
                }
            }
        }
    }
}