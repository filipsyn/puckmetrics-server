package dev.synek.puckmetrics.features.games.stats.teams

import dev.synek.puckmetrics.features.stats.PlayerSeasonFaceOffProjection

data class PlayerSeasonFaceOffProjectionImpl(
    override val season: String,
    override val playerId: Int,
    override val firstName: String,
    override val lastName: String,
    override val faceOffsTaken: Int,
    override val faceOffWins: Int
) : PlayerSeasonFaceOffProjection
