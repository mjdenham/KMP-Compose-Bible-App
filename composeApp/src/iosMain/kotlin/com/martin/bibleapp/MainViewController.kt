package com.martin.bibleapp

import androidx.compose.ui.window.ComposeUIViewController
import com.martin.bibleapp.ui.App
import com.martin.bibleapp.di.initializeKoin
import com.martin.bibleapp.di.iosAppModule

fun MainViewController() = ComposeUIViewController {
    initializeKoin {
        modules(iosAppModule)
    }
    App()
}