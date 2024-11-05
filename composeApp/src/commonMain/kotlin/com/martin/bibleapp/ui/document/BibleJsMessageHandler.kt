package com.martin.bibleapp.ui.document

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class BibleJsMessageHandler : IJsMessageHandler {
    override fun methodName(): String = "currentVerse"

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        println("Greet Handler received the message, method: ${message.methodName} params: ${message.params}")
    }
}