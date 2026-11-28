package com.gghez.game2048.data.history

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.historyDataStore by preferencesDataStore(name = "history")

/** Local-only play history, newest first, capped at [MAX] entries. */
class HistoryRepository(private val context: Context) {
    private val key = stringPreferencesKey("records")

    val records: Flow<List<GameRecord>> = context.historyDataStore.data.map { p ->
        split(p[key]).mapNotNull { GameRecord.parse(it) }
    }

    suspend fun add(record: GameRecord) {
        context.historyDataStore.edit { p ->
            val updated = (listOf(record.serialize()) + split(p[key])).take(MAX)
            p[key] = updated.joinToString(";")
        }
    }

    private fun split(raw: String?): List<String> =
        raw.orEmpty().split(";").filter { it.isNotBlank() }

    companion object {
        const val MAX = 50
    }
}