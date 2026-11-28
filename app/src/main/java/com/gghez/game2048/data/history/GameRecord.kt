package com.gghez.game2048.data.history

/** One finished (or abandoned) game, stored locally on the device only. */
data class GameRecord(
    val endedAtMillis: Long,
    val score: Int,
    val bestTile: Int,
    val moves: Int,
    val elapsedSeconds: Long,
) {
    fun serialize(): String =
        listOf(endedAtMillis, score, bestTile, moves, elapsedSeconds).joinToString(",")

    companion object {
        fun parse(s: String): GameRecord? = runCatching {
            val p = s.split(",")
            GameRecord(
                endedAtMillis = p[0].toLong(),
                score = p[1].toInt(),
                bestTile = p[2].toInt(),
                moves = p[3].toInt(),
                elapsedSeconds = p[4].toLong(),
            )
        }.getOrNull()
    }
}