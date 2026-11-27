package com.gghez.game2048.data.history

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.historyDataStore by preferencesDataStore(name = "history")

class HistoryRepository(private val context: Context) {
    private val key = stringPreferencesKey("records")

    /** Newest first. */
    val records: Flow<List<GameRecord>> = context.historyDataStore.data.map { p ->
        p[key].orEmpty().lines().mapNotNull { GameRecord.parse(it) }
    }

    suspend fun add(record: GameRecord) {
        context.historyDataStore.edit { p ->
            val current = p[key].orEmpty().lines().filter { it.isNotBlank() }
            p[key] = (listOf(record.serialize()) + current).take(MAX_RECORDS).joinToString("\n")
        }
    }

    companion object {
        const val MAX_RECORDS = 50
    }
}