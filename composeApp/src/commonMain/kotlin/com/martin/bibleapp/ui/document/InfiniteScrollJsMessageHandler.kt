package com.martin.bibleapp.ui.document

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.atomicfu.atomic

class InfiniteScrollJsMessageHandler(
    val getPreviousPage: ((String) -> Unit) -> Unit,
    val getNextPage: ((String) -> Unit) -> Unit
) : IJsMessageHandler {
    companion object {
        const val INFINITE_SCROLL_BRIDGE_METHOD = "INFINITE_SCROLL"
        const val NEXT_PAGE = "next_page"
        const val PREVIOUS_PAGE = "previous_page"

        private var callers = atomic(0)
    }
    override fun methodName(): String = INFINITE_SCROLL_BRIDGE_METHOD

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        val newCallerCount = callers.getAndIncrement()
        println("Infinite scroll handler called Caller count: ${newCallerCount}")
        if (newCallerCount > 0) {
            println("ZZZZZ Infinite scroll handler called AGAIN")
            callers.decrementAndGet()
            return
        }
        when (message.params) {
            PREVIOUS_PAGE ->
                getPreviousPage { page: String ->
                    callback(escapeEcmaScript(page))
                    callers.decrementAndGet()
                }

            NEXT_PAGE ->
                getNextPage { page: String ->
                    callback(escapeEcmaScript(page))
                    callers.decrementAndGet()
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