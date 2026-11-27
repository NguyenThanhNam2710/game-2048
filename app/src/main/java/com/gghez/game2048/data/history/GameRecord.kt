package com.gghez.game2048.data.history

data class GameRecord(
    val timestamp: Long,
    val score: Int,
    val moves: Int,
    val elapsedSeconds: Long,
    val maxTile: Int,
) {
    fun serialize(): String = "$timestamp;$score;$moves;$elapsedSeconds;$maxTile"

    companion object {
        fun parse(s: String): GameRecord? = runCatching {
            val p = s.split(";")
            GameRecord(p[0].toLong(), p[1].toInt(), p[2].toInt(), p[3].toLong(), p[4].toInt())
        }.getOrNull()
    }
}