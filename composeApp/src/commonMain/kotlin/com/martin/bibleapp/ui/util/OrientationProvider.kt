package com.martin.bibleapp.ui.util

import androidx.compose.runtime.Composable

interface OrientationProvider {
    enum class Orientation {
        Landscape,
        Portrait
    }

    @Composable
    fun getOrientation(): Orientation
}