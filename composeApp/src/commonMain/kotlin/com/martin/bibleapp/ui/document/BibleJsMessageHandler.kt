package com.martin.bibleapp.ui.document

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class BibleJsMessageHandler(val updateVerse: (String) -> Unit) : IJsMessageHandler {
    override fun methodName(): String = "currentVerse"

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        updateVerse(message.params)
    }
}