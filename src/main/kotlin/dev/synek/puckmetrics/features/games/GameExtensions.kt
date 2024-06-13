package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.contracts.GameInfoResponse

fun Game.toInfoResponse() = GameInfoResponse(
    id = id,
    homeTeamId = homeTeamId,
    homeGoals = homeGoals,
    awayTeamId = awayTeamId,
    awayGoals = awayGoals,
)
