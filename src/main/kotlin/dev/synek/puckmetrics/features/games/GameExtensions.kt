package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.contracts.CreateGameRequest
import dev.synek.puckmetrics.contracts.GameDetailsResponse
import dev.synek.puckmetrics.contracts.GameInfoResponse
import dev.synek.puckmetrics.features.teams.toResponse

fun Game.toInfoResponse() = GameInfoResponse(
    id = id,
    homeTeamId = homeTeam?.id,
    homeGoals = homeGoals,
    awayTeamId = awayTeam?.id,
    awayGoals = awayGoals,
)

fun Game.toDetailsResponse() = GameDetailsResponse(
    id = id,
    season = season,
    type = type,
    dateTimeUtc = dateTimeUtc,
    homeTeam = homeTeam?.toResponse(),
    homeGoals = homeGoals,
    awayTeam = awayTeam?.toResponse(),
    awayGoals = awayGoals,
    outcome = outcome,
    venueName = venueName,
)

fun CreateGameRequest.toEntity() = Game(
    season = season,
    type = type,
    dateTimeUtc = dateTimeUtc,
    awayTeamId = awayTeamId,
    homeTeamId = homeTeamId,
    awayGoals = awayGoals,
    homeGoals = homeGoals,
    outcome = outcome,
    venueName = venueName,
)
