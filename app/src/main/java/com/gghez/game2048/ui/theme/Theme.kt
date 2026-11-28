package com.gghez.game2048.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightScheme = lightColorScheme(
    background = AppBackgroundLight,
    surface = SurfaceLight,
    onBackground = BodyTextLight,
    onSurface = BodyTextLight,
    primary = Accent,
    onPrimary = Snow,
    secondaryContainer = Color(0xFFFFC9DE),
    onSecondaryContainer = Color(0xFF7A1F4B),
)

private val DarkScheme = darkColorScheme(
    background = AppBackgroundDark,
    surface = SurfaceDark,
    onBackground = BodyTextDark,
    onSurface = BodyTextDark,
    primary = Accent,
    onPrimary = Snow,
    secondaryContainer = Color(0xFF5A2A42),
    onSecondaryContainer = Color(0xFFFFD1E2),
)

// Extra-round corners everywhere for the "squishy" look.
private val CuteShapes = Shapes(
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun Game2048Theme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        shapes = CuteShapes,
        content = content,
    )
}