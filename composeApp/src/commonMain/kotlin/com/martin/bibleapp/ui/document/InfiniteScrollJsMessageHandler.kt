package com.martin.bibleapp.ui.document

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class InfiniteScrollJsMessageHandler(
    val getPreviousPage: ((String) -> Unit) -> Unit,
    val getNextPage: ((String) -> Unit) -> Unit
) : IJsMessageHandler {
    companion object {
        const val INFINITE_SCROLL_BRIDGE_METHOD = "INFINITE_SCROLL"
        const val NEXT_PAGE = "next_page"
        const val PREVIOUS_PAGE = "previous_page"
    }
    override fun methodName(): String = INFINITE_SCROLL_BRIDGE_METHOD

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        println("Infinite scroll handler called")
        when (message.params) {
            PREVIOUS_PAGE ->
                getPreviousPage { page: String ->
                    callback(escapeEcmaScript(page))
                }

            NEXT_PAGE ->
                getNextPage { page: String ->
                    callback(escapeEcmaScript(page))
                }

            else -> println("Error: Unknown message: $message")
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