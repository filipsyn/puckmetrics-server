package dev.synek.puckmetrics.contracts


data class PlayerSeasonGoalsResponse(
    val playerId: Long?,
    val firstName: String,
    val lastName: String,
    val season: Int,
    val totalGoals: Long
)
