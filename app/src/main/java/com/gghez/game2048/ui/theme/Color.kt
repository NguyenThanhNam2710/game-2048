package com.gghez.game2048.ui.theme

import androidx.compose.ui.graphics.Color

// Aurora palette
val Ink = Color(0xFF2E2A45)
val Snow = Color(0xFFFFFFFF)

val AppBackgroundLight = Color(0xFFF4F1FA)
val SurfaceLight = Color(0xFFE8E2F6)
val BodyTextLight = Color(0xFF5B5675)
val EmphasizedTextLight = Ink

val AppBackgroundDark = Color(0xFF14121F)
val SurfaceDark = Color(0xFF231F36)
val BodyTextDark = Color(0xFFB9B3D1)
val EmphasizedTextDark = Color(0xFFE6E1F5)

// Board & empty cells
val BoardLight = Color(0xFFDDD5F0)
val EmptyCellLight = Color(0xFFEEE9F9)
val BoardDark = Color(0xFF231F36)
val EmptyCellDark = Color(0xFF181526)

val Accent = Color(0xFF7C5CFF)

object TileColors {
    private val Violet = Color(0xFF9B7BFF)
    private val Indigo = Color(0xFF7C5CFF)
    private val Blue = Color(0xFF4F8CFF)
    private val Teal = Color(0xFF22B8CF)
    private val Mint = Color(0xFF2FBF8F)
    private val Lime = Color(0xFF8BC34A)
    private val Amber = Color(0xFFF5B83D)
    private val Orange = Color(0xFFFF8A4C)
    private val Pink = Color(0xFFFF5C7A)
    private val Hot = Color(0xFFFF3D68)

    private val accent = mapOf(
        8 to Violet, 16 to Indigo, 32 to Blue, 64 to Teal, 128 to Mint,
        256 to Lime, 512 to Amber, 1024 to Orange, 2048 to Pink,
    )

    fun bg(value: Int, dark: Boolean): Color = when (value) {
        2 -> if (dark) Color(0xFF2E2948) else Color(0xFFE4DCF7)
        4 -> if (dark) Color(0xFF3C3560) else Color(0xFFCFC2F2)
        else -> accent[value] ?: Hot
    }

    fun fg(value: Int, dark: Boolean): Color = when (value) {
        2, 4 -> if (dark) Color(0xFFE6E1F5) else Ink
        512 -> Ink // vàng sáng, chữ trắng khó đọc
        else -> Snow
    }
}