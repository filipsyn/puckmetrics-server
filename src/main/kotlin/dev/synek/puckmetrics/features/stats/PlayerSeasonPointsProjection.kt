package dev.synek.puckmetrics.features.stats

interface PlayerSeasonPointsProjection {
    val season: String
    val playerId: Long
    val firstName: String
    val lastName: String
    val totalPoints: Int
}
