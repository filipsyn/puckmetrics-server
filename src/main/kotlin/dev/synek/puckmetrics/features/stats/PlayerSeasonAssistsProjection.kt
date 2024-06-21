package dev.synek.puckmetrics.features.stats

interface PlayerSeasonAssistsProjection {
    val playerId: Long
    val firstName: String
    val lastName: String
    val season: Int
    val totalAssists: Long
}
