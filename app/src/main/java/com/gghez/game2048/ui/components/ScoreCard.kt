package com.gghez.game2048.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gghez.game2048.ui.theme.Bubblegum
import com.gghez.game2048.ui.theme.Lavender

@Composable
fun ScoreCard(
    label: String,
    value: Int,
    highlighted: Boolean,
    modifier: Modifier = Modifier,
    emoji: String = "",
) {
    val shape = RoundedCornerShape(20.dp)
    // Highlighted card: bubblegum -> lavender candy gradient. Other: soft surface.
    val background = if (highlighted) {
        Brush.horizontalGradient(
            listOf(Bubblegum.copy(alpha = 0.45f), Lavender.copy(alpha = 0.45f)),
        )
    } else {
        Brush.horizontalGradient(
            listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surface),
        )
    }
    Column(
        modifier = modifier
            .clip(shape)
            .background(background)
            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), shape)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            if (emoji.isEmpty()) label else "$emoji $label",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
        )
        Text(
            value.toString(),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}