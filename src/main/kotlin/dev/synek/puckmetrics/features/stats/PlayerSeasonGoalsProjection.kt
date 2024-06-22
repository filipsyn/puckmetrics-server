package dev.synek.puckmetrics.features.stats

interface PlayerSeasonGoalsProjection {
    val playerId: Long
    val firstName: String
    val lastName: String
    val season: String
    val totalGoals: Long
}
