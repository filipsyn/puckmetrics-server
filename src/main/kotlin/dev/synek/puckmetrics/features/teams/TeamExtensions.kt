package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.contracts.CreateUpdateTeamRequest
import dev.synek.puckmetrics.contracts.TeamInfoResponse

fun Team.toResponse() = TeamInfoResponse(
    id = id,
    location = location,
    name = name,
)

fun CreateUpdateTeamRequest.toEntity() = Team(
    location = location,
    name = name,
    abbreviation = abbreviation,
)
