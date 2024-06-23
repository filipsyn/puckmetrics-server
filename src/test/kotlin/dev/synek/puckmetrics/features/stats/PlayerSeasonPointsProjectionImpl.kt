package dev.synek.puckmetrics.features.stats

data class PlayerSeasonPointsProjectionImpl(
    override val season: String,
    override val playerId: Long,
    override val firstName: String,
    override val lastName: String,
    override val totalPoints: Int,
) : PlayerSeasonPointsProjection
