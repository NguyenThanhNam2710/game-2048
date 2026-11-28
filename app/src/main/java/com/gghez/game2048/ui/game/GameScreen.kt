package com.gghez.game2048.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gghez.game2048.R
import com.gghez.game2048.data.settings.ThemeMode
import com.gghez.game2048.domain.Direction
import com.gghez.game2048.domain.GameStatus
import com.gghez.game2048.ui.components.GameGrid
import com.gghez.game2048.ui.components.NewGameDialog
import com.gghez.game2048.ui.components.ScoreCard
import com.gghez.game2048.ui.components.TipsCarousel
import androidx.compose.material.icons.filled.History

/**
 * Portrait layout, bottom-anchored for thumb reach:
 *
 *   header (title + leaderboard + settings)
 *   ---- flexible space ----
 *   score row
 *   time · moves chips
 *   full-width square board
 *   tips card
 *   action bar (undo · new game · premium)
 *
 * The board is ALWAYS full board width and never shrinks. The flexible space
 * collapses first on short screens.
 */
@Composable
fun GameScreen(
    ui: GameUiState,
    onSwipe: (Direction) -> Unit,
    onNewGame: () -> Unit,
    onConfirmNewGame: () -> Unit,
    onDismissDialog: () -> Unit,
    onUndo: () -> Unit,
    onContinue: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenPremiumScreen: () -> Unit,
) {
    val dark = ui.settings.theme == ThemeMode.DARK
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Header: compact, only the two secondary actions live here.
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            AppTitle(modifier = Modifier.weight(1f))
            IconButton(onClick = onOpenHistory) {
                Icon(Icons.Default.History, stringResource(R.string.history_title))
            }
            IconButton(onClick = onOpenLeaderboard) {
                Icon(Icons.Default.EmojiEvents, stringResource(R.string.cd_leaderboard))
            }
            IconButton(onClick = onOpenSettings) {
                Icon(Icons.Default.Settings, stringResource(R.string.cd_settings))
            }
        }

        // Flexible space pushes everything below toward the thumb.
        Spacer(Modifier.weight(1f))

        // Score row
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ScoreCard(stringResource(R.string.score), ui.state.score, highlighted = true, modifier = Modifier.weight(1f))
            ScoreCard(stringResource(R.string.best_score), ui.bestScore, highlighted = false, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(8.dp))

        // Time and move count as two small chips, centered above the board.
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatChip(formatTime(ui.elapsedSeconds))
            Spacer(Modifier.width(8.dp))
            StatChip("${ui.state.moves} ${stringResource(R.string.moves_suffix)}")
        }
        Spacer(Modifier.height(10.dp))

        // Board — ALWAYS full width (square), never shrinks.
        Box(Modifier.fillMaxWidth()) {
            GameGrid(
                board = ui.state.board,
                dark = dark,
                fastAnimations = ui.settings.fastAnimations,
                onSwipe = onSwipe,
                modifier = Modifier.fillMaxWidth(),
            )
            when (ui.state.status) {
                GameStatus.WON -> Overlay(stringResource(R.string.you_win)) {
                    TextButton(onClick = onContinue) { Text(stringResource(R.string.continue_playing)) }
                    TextButton(onClick = onConfirmNewGame) { Text(stringResource(R.string.start)) }
                }
                GameStatus.LOST -> Overlay(stringResource(R.string.game_over)) {
                    TextButton(onClick = onConfirmNewGame) { Text(stringResource(R.string.start)) }
                }
                GameStatus.PLAYING -> {}
            }
        }
        Spacer(Modifier.height(10.dp))

        // Rotating tips card (fixed two-line height — never reflows the layout).
        TipsCarousel(fastAnimations = ui.settings.fastAnimations)
        Spacer(Modifier.height(10.dp))

        // Action bar at the bottom for easy thumb reach.
        // Undo is always rendered (disabled when nothing to undo) so the bar keeps a constant height.
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledTonalIconButton(
                onClick = onUndo,
                enabled = ui.canUndo,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(Icons.AutoMirrored.Filled.Undo, stringResource(R.string.undo))
            }
            FilledTonalIconButton(
                onClick = onNewGame,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(Icons.Default.Refresh, stringResource(R.string.cd_restart))
            }
            FilledTonalIconButton(
                onClick = onOpenPremiumScreen,
                modifier = Modifier.size(56.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = stringResource(R.string.premium_title),
                    tint = Color.Unspecified, // keep the gold of the crown vector
                )
            }
        }
    }
    if (ui.showNewGameDialog) NewGameDialog(onConfirm = onConfirmNewGame, onDismiss = onDismissDialog)
}

@Composable
private fun BoxScope.Overlay(title: String, actions: @Composable () -> Unit) {
    Box(
        Modifier
            .matchParentSize()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.88f))
            // Swallow every pointer event so taps and drags never reach the board
            // behind the overlay. The action buttons sit above this Box and keep
            // their own clicks; this only blocks the translucent backdrop area.
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent().changes.forEach { it.consume() }
                    }
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            actions()
        }
    }
}

/** The app wordmark, kept to a single line so it never wraps next to the header icons. */
@Composable
private fun AppTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.app_name),
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

/** Small rounded pill used for the timer and the move counter. */
@Composable
private fun StatChip(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

private fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}