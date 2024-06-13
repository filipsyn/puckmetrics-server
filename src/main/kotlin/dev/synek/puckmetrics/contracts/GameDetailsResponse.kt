package dev.synek.puckmetrics.contracts

data class GameDetailsResponse(
    val id: Long? = null,

    val season: String? = null,

    val type: String? = null,

    val dateTimeUtc: String? = null,

    val awayTeamId: Long? = null,

    val homeTeamId: Long? = null,

    val awayGoals: Int = 0,

    val homeGoals: Int = 0,

    val outcome: String? = null,

    val venueName: String? = null,
)
