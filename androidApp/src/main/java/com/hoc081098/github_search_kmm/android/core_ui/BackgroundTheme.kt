package com.hoc081098.github_search_kmm.android.core_ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * A class to model background color and tonal elevation values for App.
 */
@Immutable
data class BackgroundTheme(val color: Color = Color.Unspecified, val tonalElevation: Dp = Dp.Unspecified)

/**
 * A composition local for [BackgroundTheme].
 */
val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }
