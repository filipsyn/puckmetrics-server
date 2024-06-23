package dev.synek.puckmetrics.contracts

data class PlayerSeasonFaceOffResponse(
    val season: String,
    val playerId: Int,
    val firstName: String,
    val lastName: String,
    val faceOffWinPercentage: Double,
)
