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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScoreCard(label: String, value: Int, highlighted: Boolean, modifier: Modifier = Modifier) {
    val bg = if (highlighted) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surface
    val fg = if (highlighted) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onBackground
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
            color = fg.copy(alpha = 0.75f), textAlign = TextAlign.Center)
        Text(value.toString(), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = fg)
    }
}
