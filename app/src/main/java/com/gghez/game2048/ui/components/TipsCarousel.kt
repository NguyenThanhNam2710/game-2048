package com.gghez.game2048.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gghez.game2048.R
import kotlinx.coroutines.delay

private const val TIP_ROTATION_MS = 5_000L

// Cute mascots that rotate together with the tips.
private val TIP_EMOJIS = listOf("🐰", "🌸", "🧸", "🍓", "🦄", "🎀", "🌈")

/**
 * A small soft card that shows one gameplay tip at a time and rotates to the next
 * one every few seconds, looping. The rotation index is purely local presentation
 * state, so it lives here rather than in GameUiState. Tips come from the localized
 * `game_tips` string array.
 */
@Composable
fun TipsCarousel(fastAnimations: Boolean, modifier: Modifier = Modifier) {
    val tips = stringArrayResource(R.array.game_tips)
    if (tips.isEmpty()) return

    var index by remember { mutableIntStateOf(0) }
    LaunchedEffectRotation(tipCount = tips.size) { index = (index + 1) % tips.size }

    val tipLabel = stringResource(R.string.cd_tip)
    val shape = RoundedCornerShape(20.dp)
    val fadeMs = if (fastAnimations) 150 else 400
    Crossfade(
        targetState = index,
        animationSpec = tween(fadeMs),
        label = "tip",
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), shape)
            .heightIn(min = 52.dp),
    ) { i ->
        Row(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                TIP_EMOJIS[i % TIP_EMOJIS.size],
                fontSize = 24.sp,
                modifier = Modifier.semantics { contentDescription = tipLabel },
            )
            Text(
                tips[i],
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                // Reserve a constant two-line height so the card never resizes as tips
                // rotate and never shifts the layout around it.
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * Advances the tip on a fixed interval. Restarts only when the tip count changes;
 * a single tip never rotates.
 */
@Composable
private fun LaunchedEffectRotation(tipCount: Int, advance: () -> Unit) {
    LaunchedEffect(tipCount) {
        if (tipCount <= 1) return@LaunchedEffect
        while (true) {
            delay(TIP_ROTATION_MS)
            advance()
        }
    }
}