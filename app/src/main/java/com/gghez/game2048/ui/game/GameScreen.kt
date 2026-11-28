package com.gghez.game2048.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.gghez.game2048.ui.components.TipsCarousel
import com.gghez.game2048.ui.theme.Bubblegum
import com.gghez.game2048.ui.theme.Lavender

/**
 * Portrait layout, "banner on top, dock at the bottom":
 *
 *   header (title + premium crown)
 *   score banner (best · SCORE · time/moves)
 *   tips card
 *   ---- flexible space ----
 *   full-width square board (vertically centred)
 *   ---- flexible space ----
 *   floating dock (history · leaderboard · UNDO · new game · settings)
 *
 * The board is ALWAYS full board width and never shrinks. The two flexible
 * spaces collapse first on short screens.
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
    Box(Modifier.fillMaxSize()) {
        Decorations()
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            // Header: title on the left, premium crown on the right.
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                AppTitle(modifier = Modifier.weight(1f))
                IconButton(onClick = onOpenPremiumScreen) {
                    Icon(
                        painter = painterResource(R.drawable.ic_crown),
                        contentDescription = stringResource(R.string.premium_title),
                        tint = Color.Unspecified, // keep the gold of the crown vector
                    )
                }
            }
            Spacer(Modifier.height(8.dp))

            // One banner for score, best score, time and moves.
            ScoreBanner(
                score = ui.state.score,
                best = ui.bestScore,
                elapsedSeconds = ui.elapsedSeconds,
                moves = ui.state.moves,
            )
            Spacer(Modifier.height(10.dp))

            // Rotating tips card (fixed two-line height — never reflows the layout).
            TipsCarousel(fastAnimations = ui.settings.fastAnimations)

            // Flexible space above the board.
            Spacer(Modifier.weight(1f))

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
                    GameStatus.WON -> Overlay("🎉 ${stringResource(R.string.you_win)} 🎉") {
                        TextButton(onClick = onContinue) { Text("🌸 " + stringResource(R.string.continue_playing)) }
                        TextButton(onClick = onConfirmNewGame) { Text("💖 " + stringResource(R.string.start)) }
                    }
                    GameStatus.LOST -> Overlay("🥺 ${stringResource(R.string.game_over)}") {
                        TextButton(onClick = onConfirmNewGame) { Text("💖 " + stringResource(R.string.start)) }
                    }
                    GameStatus.PLAYING -> {}
                }
            }

            // Flexible space below the board.
            Spacer(Modifier.weight(1f))

            // Floating dock at the bottom for easy thumb reach.
            // Undo is always rendered (disabled when nothing to undo) so the dock keeps a constant height.
            BottomDock(
                canUndo = ui.canUndo,
                onHistory = onOpenHistory,
                onLeaderboard = onOpenLeaderboard,
                onUndo = onUndo,
                onNewGame = onNewGame,
                onSettings = onOpenSettings,
            )
        }
    }
    if (ui.showNewGameDialog) NewGameDialog(onConfirm = onConfirmNewGame, onDismiss = onDismissDialog)
}

/** Single gradient card: best score (left), current score (centre, big), time + moves (right). */
@Composable
private fun ScoreBanner(score: Int, best: Int, elapsedSeconds: Long, moves: Int) {
    val shape = RoundedCornerShape(28.dp)
    val ink = MaterialTheme.colorScheme.onBackground
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    listOf(Bubblegum.copy(alpha = 0.55f), Lavender.copy(alpha = 0.55f)),
                ),
            )
            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), shape)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            Text(
                "👑 ${stringResource(R.string.best_score)}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = ink.copy(alpha = 0.8f),
                maxLines = 2,
            )
            Text(
                best.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ink,
                maxLines = 1,
            )
        }
        Column(Modifier.weight(1.2f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "🎀 ${stringResource(R.string.score)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ink.copy(alpha = 0.8f),
                maxLines = 1,
            )
            Text(
                score.toString(),
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ink,
                maxLines = 1,
            )
        }
        Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(
                "⏰ ${formatTime(elapsedSeconds)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = ink,
                maxLines = 1,
            )
            Text(
                "🐾 $moves ${stringResource(R.string.moves_suffix)}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ink.copy(alpha = 0.85f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Pill-shaped floating dock; Undo is the big round button in the middle. */
@Composable
private fun BottomDock(
    canUndo: Boolean,
    onHistory: () -> Unit,
    onLeaderboard: () -> Unit,
    onUndo: () -> Unit,
    onNewGame: () -> Unit,
    onSettings: () -> Unit,
) {
    val shape = RoundedCornerShape(36.dp)
    Row(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), shape)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DockButton(Icons.Default.History, stringResource(R.string.history_title), onHistory)
        DockButton(Icons.Default.EmojiEvents, stringResource(R.string.cd_leaderboard), onLeaderboard)
        FilledIconButton(
            onClick = onUndo,
            enabled = canUndo,
            modifier = Modifier.size(60.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.Undo, stringResource(R.string.undo))
        }
        DockButton(Icons.Default.Refresh, stringResource(R.string.cd_restart), onNewGame)
        DockButton(Icons.Default.Settings, stringResource(R.string.cd_settings), onSettings)
    }
}

@Composable
private fun DockButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
        Icon(icon, description, tint = MaterialTheme.colorScheme.primary)
    }
}

/** Faint emoji row that peeks out in the free space above the dock. Never intercepts touches. */
@Composable
private fun BoxScope.Decorations() {
    Text(
        "🌸 ✨ 🎀 ✨ 🌸",
        fontSize = 28.sp,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 96.dp)
            .alpha(0.3f),
    )
}

@Composable
private fun BoxScope.Overlay(title: String, actions: @Composable () -> Unit) {
    Box(
        Modifier
            .matchParentSize()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
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

/** The app wordmark, kept to a single line so it never wraps next to the crown. */
@Composable
private fun AppTitle(modifier: Modifier = Modifier) {
    Text(
        text = "🎀 ${stringResource(R.string.app_name)}",
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

private fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}