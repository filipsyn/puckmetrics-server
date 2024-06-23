package dev.synek.puckmetrics.features.stats

interface PlayerSeasonFaceOffProjection {
    val season: String
    val playerId: Int
    val firstName: String
    val lastName: String
    val faceOffsTaken: Int
    val faceOffWins: Int
}
