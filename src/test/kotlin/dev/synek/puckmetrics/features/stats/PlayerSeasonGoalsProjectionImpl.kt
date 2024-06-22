package dev.synek.puckmetrics.features.stats

data class PlayerSeasonGoalsProjectionImpl(
    override val playerId: Long,
    override val season: String,
    override val firstName: String,
    override val lastName: String,
    override val totalGoals: Long
) : PlayerSeasonGoalsProjection
