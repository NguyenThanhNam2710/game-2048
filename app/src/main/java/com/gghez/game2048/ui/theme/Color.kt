package com.gghez.game2048.ui.theme

import androidx.compose.ui.graphics.Color

// "Candy Blossom" palette: bubblegum pinks, lavender and peach for a cute, soft feel.
// Base tones (names kept so other files need no changes)
val Base03 = Color(0xFF2A1220) // darkest background (dark mode)
val Base02 = Color(0xFF3D1B2E) // dark surface
val Base01 = Color(0xFF7A3553) // light-mode emphasized text
val Base00 = Color(0xFF9A4A6C) // light-mode body text
val Base0 = Color(0xFFF2B5CE)  // dark-mode body text
val Base1 = Color(0xFFFFD3E4)  // dark-mode emphasized text
val Base2 = Color(0xFFFFE0EC)  // light surface
val Base3 = Color(0xFFFFF0F5)  // lightest background (light mode)

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
val BoardLight = Color(0xFFFFC7DD)
val EmptyCellLight = Color(0xFFFFDCEA)
val BoardDark = Color(0xFF4A2238)
val EmptyCellDark = Color(0xFF351728)

// Shared accent for primary controls
val Accent = Color(0xFFE85D9A)

// Text/icon colour on top of the accent (buttons, switches)
val Snow = Color(0xFFFFF9FC)

// Extra candy colours used for gradients and decorations
val Bubblegum = Color(0xFFFF8FBD)
val Lavender = Color(0xFFC9A7F5)
val Peach = Color(0xFFFFB38A)

object TileColors {
    // Gradient: soft pink -> hot pink -> violet -> lavender -> coral -> peach -> gold.
    // Pair = (background, text).
    private val accents = mapOf(
        8 to (Color(0xFFFFB6CF) to Color(0xFF6B1F42)),
        16 to (Color(0xFFFF94BD) to Color(0xFF5A0F33)),
        32 to (Color(0xFFFF77AC) to Color.White),
        64 to (Color(0xFFF25C9C) to Color.White),
        128 to (Color(0xFFD96AD0) to Color.White),
        256 to (Color(0xFFB58AF0) to Color.White),
        512 to (Color(0xFFFF9E9E) to Color(0xFF6B1E2E)),
        1024 to (Color(0xFFFFC48A) to Color(0xFF5A2D0A)),
        2048 to (Color(0xFFFFD76A) to Color(0xFF5A3D00)),
    )
    // Gold emphasis for anything beyond 2048.
    private val fallback = Color(0xFFFFD76A) to Color(0xFF5A3D00)

    fun bg(value: Int, dark: Boolean): Color = when (value) {
        2 -> if (dark) Color(0xFF5A2A42) else Color(0xFFFFE6F0)
        4 -> if (dark) Color(0xFF6B3050) else Color(0xFFFFD0E3)
        else -> (accents[value] ?: fallback).first
    }

    fun fg(value: Int, dark: Boolean): Color = when (value) {
        2 -> if (dark) Color(0xFFF7C3D8) else Color(0xFFB0597F)
        4 -> if (dark) Color(0xFFFFD1E2) else Color(0xFF9E4268)
        else -> (accents[value] ?: fallback).second
    }

    /** Tiny mascot shown in the corner of each tile. */
    fun emoji(value: Int): String = when (value) {
        2 -> "🌱"
        4 -> "🌷"
        8 -> "🌸"
        16 -> "🍓"
        32 -> "🍰"
        64 -> "🧁"
        128 -> "🎀"
        256 -> "🦄"
        512 -> "🌈"
        1024 -> "💎"
        else -> if (value >= 2048) "👑" else ""
    }
}