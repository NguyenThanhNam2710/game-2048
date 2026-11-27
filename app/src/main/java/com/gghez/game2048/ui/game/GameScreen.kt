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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.gghez.game2048.R
import com.gghez.game2048.data.settings.ThemeMode
import com.gghez.game2048.domain.Direction
import com.gghez.game2048.domain.GameStatus
import com.gghez.game2048.ui.components.GameGrid
import com.gghez.game2048.ui.components.NewGameDialog
import com.gghez.game2048.ui.components.PremiumButton
import com.gghez.game2048.ui.components.ScoreCard
import com.gghez.game2048.ui.components.TipsCarousel

/**
 * Portrait layout, bottom-anchored for thumb reach: the header and the rotating tips card
 * sit at the top; a flexible spacer then pushes the score row, the full-width square board
 * and the footer (timer, undo, move count) to the bottom. The board is ALWAYS full board
 * width and never shrinks — the tips and score blocks are sized down instead so everything
 * fits above the system bars on short screens.
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
    onOpenPremiumScreen: () -> Unit,
    onOpenHistory: () -> Unit,
) {
    val dark = ui.settings.theme == ThemeMode.DARK
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Top: title on the left, timer + moves stacked on the right
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            AppTitle()
            Spacer(Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Text(formatTime(ui.elapsedSeconds), fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(
                    "${ui.state.moves} ${stringResource(R.string.moves_suffix)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        // Scores
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ScoreCard(stringResource(R.string.score), ui.state.score, highlighted = true, modifier = Modifier.weight(1f))
            ScoreCard(stringResource(R.string.best_score), ui.bestScore, highlighted = false, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        // Tips
        TipsCarousel(fastAnimations = ui.settings.fastAnimations)

        // Board centered in the remaining space (always full width, square)
        Spacer(Modifier.weight(1f))
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
        Spacer(Modifier.weight(1f))

        // Bottom action bar
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onOpenPremiumScreen) {
                Icon(
                    painter = painterResource(R.drawable.ic_crown),
                    contentDescription = stringResource(R.string.premium_title),
                    tint = Color.Unspecified, // giữ màu cam của ic_crown
                )
            }
//            IconButton(onClick = onOpenLeaderboard) { Icon(Icons.Default.EmojiEvents, stringResource(R.string.cd_leaderboard)) }
            IconButton(onClick = onOpenHistory) { Icon(Icons.Default.History, stringResource(R.string.history_title)) }
            IconButton(onClick = onUndo, enabled = ui.canUndo) {
                Icon(Icons.AutoMirrored.Filled.Undo, stringResource(R.string.undo))
            }
            IconButton(onClick = onNewGame) { Icon(Icons.Default.Refresh, stringResource(R.string.cd_restart)) }
            IconButton(onClick = onOpenSettings) { Icon(Icons.Default.Settings, stringResource(R.string.cd_settings)) }
        }
    }
    if (ui.showNewGameDialog) NewGameDialog(onConfirm = onConfirmNewGame, onDismiss = onDismissDialog)
}

@Composable
private fun BoxScope.Overlay(title: String, actions: @Composable () -> Unit) {
    Box(
        Modifier
            .matchParentSize()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
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

/**
 * The app wordmark rendered as the mathematical notation 2¹¹ (= 2048): the digits after the
 * "exp" separator in the app name are shown as a smaller, raised superscript. Falls back to
 * the raw name if it has no "exp" separator.
 */
@Composable
private fun AppTitle(modifier: Modifier = Modifier) {
    val name = stringResource(R.string.app_name)
    val separator = "exp"
    val rendered = if (name.contains(separator)) {
        val (base, exponent) = name.split(separator, limit = 2)
        buildAnnotatedString {
            append(base)
            withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.6.em)) {
                append(exponent)
            }
        }
    } else {
        AnnotatedString(name)
    }
    Text(
        rendered,
        modifier = modifier,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

private fun formatTime(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}
