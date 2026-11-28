//package com.gghez.game2048.ui.history
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.ModalBottomSheet
//import androidx.compose.material3.Text
//import androidx.compose.material3.rememberModalBottomSheetState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.gghez.game2048.R
//import com.gghez.game2048.data.history.GameRecord
//import com.gghez.game2048.ui.theme.TileColors
//import java.text.DateFormat
//import java.util.Date
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HistoryBottomSheet(records: List<GameRecord>, dark: Boolean, onDismiss: () -> Unit) {
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
//        Column(
//            Modifier
//                .fillMaxWidth()
//                .navigationBarsPadding()
//                .padding(horizontal = 20.dp),
//        ) {
//            Text(
//                stringResource(R.string.history_title),
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.align(Alignment.CenterHorizontally),
//            )
//            Spacer(Modifier.height(16.dp))
//            if (records.isEmpty()) {
//                Text(
//                    stringResource(R.string.history_empty),
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .padding(vertical = 32.dp),
//                )
//            } else {
//                LazyColumn(
//                    verticalArrangement = Arrangement.spacedBy(10.dp),
//                    contentPadding = PaddingValues(bottom = 24.dp),
//                ) {
//                    items(records) { HistoryRow(it, dark) }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun HistoryRow(r: GameRecord, dark: Boolean) {
//    val date = remember(r.timestamp) {
//        DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(Date(r.timestamp))
//    }
//    val muted = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(16.dp))
//            .background(MaterialTheme.colorScheme.surface)
//            .padding(12.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(12.dp),
//    ) {
//        Box(
//            Modifier
//                .size(48.dp)
//                .clip(RoundedCornerShape(12.dp))
//                .background(TileColors.bg(r.maxTile, dark)),
//            contentAlignment = Alignment.Center,
//        ) {
//            Text(
//                r.maxTile.toString(),
//                color = TileColors.fg(r.maxTile, dark),
//                fontWeight = FontWeight.Bold,
//                fontSize = if (r.maxTile >= 1000) 13.sp else 16.sp,
//            )
//        }
//        Column(Modifier.weight(1f)) {
//            Text(r.score.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
//            Text(
//                "${r.moves} ${stringResource(R.string.moves_suffix)} · ${formatDuration(r.elapsedSeconds)}",
//                fontSize = 12.sp,
//                color = muted,
//            )
//        }
//        Text(date, fontSize = 12.sp, color = muted)
//    }
//}
//
//private fun formatDuration(seconds: Long): String = "%02d:%02d".format(seconds / 60, seconds % 60)