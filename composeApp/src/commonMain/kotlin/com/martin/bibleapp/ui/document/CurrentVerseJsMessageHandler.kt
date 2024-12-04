package com.martin.bibleapp.ui.document

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class CurrentVerseJsMessageHandler(val updateVerse: (String) -> Unit) : IJsMessageHandler {
    companion object {
        const val CURRENT_VERSE_BRIDGE_METHOD = "CURRENT_VERSE"
    }
    override fun methodName(): String = CURRENT_VERSE_BRIDGE_METHOD

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        updateVerse(message.params)
    }
}