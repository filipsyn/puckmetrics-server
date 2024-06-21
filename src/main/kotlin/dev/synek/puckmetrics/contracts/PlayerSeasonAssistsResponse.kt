package dev.synek.puckmetrics.contracts

data class PlayerSeasonAssistsResponse(
    val season: Int,
    val playerId: Long,
    val firstName: String,
    val lastName: String,
    val totalAssists: Long,
)
