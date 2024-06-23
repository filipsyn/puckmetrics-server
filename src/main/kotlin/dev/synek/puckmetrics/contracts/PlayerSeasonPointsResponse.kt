package dev.synek.puckmetrics.contracts

data class PlayerSeasonPointsResponse(
    val season: String,
    val playerId: Long,
    val firstName: String,
    val lastName: String,
    val totalPoints: Int,
)
