package com.martin.bibleapp.ui.util

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

class OrientationProviderImpl: OrientationProvider {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun getOrientation(): OrientationProvider.Orientation =
        if (calculateWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Expanded) {
            OrientationProvider.Orientation.Landscape
        } else {
            OrientationProvider.Orientation.Portrait
        }
}