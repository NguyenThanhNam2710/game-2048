package com.gghez.game2048.ui.theme

import androidx.compose.ui.graphics.Color

// "Mist & Sage" palette: soft misty greens/teals for a calm, relaxing feel.
// Base tones (names kept so Theme.kt / GameGrid.kt need no changes)
val Base03 = Color(0xFF14201F) // darkest background (dark mode)
val Base02 = Color(0xFF1D2D2B) // dark surface
val Base01 = Color(0xFF4F625A) // light-mode emphasized text
val Base00 = Color(0xFF5F746B) // light-mode body text
val Base0 = Color(0xFFA9C2BC)  // dark-mode body text
val Base1 = Color(0xFFC3D6D0)  // dark-mode emphasized text
val Base2 = Color(0xFFE3EBE2)  // light surface
val Base3 = Color(0xFFF4F7F3)  // lightest background (light mode)

// Semantic aliases used by the Material color scheme
val AppBackgroundLight = Base3
val SurfaceLight = Base2
val BodyTextLight = Base00
val EmphasizedTextLight = Base01

val AppBackgroundDark = Base03
val SurfaceDark = Base02
val BodyTextDark = Base0
val EmphasizedTextDark = Base1

// Board & empty cells
val BoardLight = Color(0xFFD5E0D4)
val EmptyCellLight = Color(0xFFE6EEE4)
val BoardDark = Color(0xFF243634)
val EmptyCellDark = Color(0xFF1A2927)

// Shared accent for primary controls
val Accent = Color(0xFF5FA8A0)

// Text/icon colour on top of the accent (buttons, switches)
val Snow = Color(0xFFFAFCFA)

object TileColors {
    // Pastel gradient from sage green -> teal -> blue -> violet -> pink -> warm gold.
    // Pair = (background, text). Dark text on light tiles, white on deep tiles.
    private val accents = mapOf(
        8 to (Color(0xFFB7D9C9) to Color(0xFF2D4A3E)),
        16 to (Color(0xFF8EC8C0) to Color(0xFF1F3F3C)),
        32 to (Color(0xFF6FB1C4) to Color(0xFF0E2F3A)),
        64 to (Color(0xFF7F9FD6) to Color(0xFF0E2250)),
        128 to (Color(0xFF8575CF) to Color.White),
        256 to (Color(0xFFB77FC9) to Color.White),
        512 to (Color(0xFFD9708F) to Color.White),
        1024 to (Color(0xFFF2A07B) to Color(0xFF3A1F12)),
        2048 to (Color(0xFFF0C75E) to Color(0xFF3A2C08)),
    )
    // Gold emphasis for anything beyond 2048.
    private val fallback = Color(0xFFF0C75E) to Color(0xFF3A2C08)

    fun bg(value: Int, dark: Boolean): Color = when (value) {
        2 -> if (dark) Color(0xFF2C4240) else Color(0xFFE6EEE4)
        4 -> if (dark) Color(0xFF35524F) else Color(0xFFD2E3D3)
        else -> (accents[value] ?: fallback).first
    }

    fun fg(value: Int, dark: Boolean): Color = when (value) {
        2 -> if (dark) Color(0xFFB5CCC6) else Color(0xFF5A6B5F)
        4 -> if (dark) Color(0xFFC9DCD7) else Color(0xFF47594D)
        else -> (accents[value] ?: fallback).second
    }
}