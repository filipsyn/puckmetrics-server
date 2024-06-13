package dev.synek.puckmetrics.contracts

data class GameInfoResponse(
    val id: Long?,
    val homeTeamId: Long?,
    val homeGoals: Int,
    val awayTeamId: Long?,
    val awayGoals: Int,
)
