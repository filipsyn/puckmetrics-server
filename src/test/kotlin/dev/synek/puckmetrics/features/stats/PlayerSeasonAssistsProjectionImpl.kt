package dev.synek.puckmetrics.features.stats

data class PlayerSeasonAssistsProjectionImpl(
    override val playerId: Long,
    override val season: String,
    override val firstName: String,
    override val lastName: String,
    override val totalAssists: Long,
) : PlayerSeasonAssistsProjection
