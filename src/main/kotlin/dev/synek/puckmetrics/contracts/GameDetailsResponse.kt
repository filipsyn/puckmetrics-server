package dev.synek.puckmetrics.contracts

import java.util.*

data class GameDetailsResponse(
    val id: Long? = null,

    val season: String? = null,

    val type: String? = null,

    val dateTimeUtc: Date? = null,

    val homeTeam: TeamInfoResponse? = null,

    val homeGoals: Int = 0,

    val awayTeam: TeamInfoResponse? = null,

    val awayGoals: Int = 0,

    val outcome: String? = null,

    val venueName: String? = null,
)
