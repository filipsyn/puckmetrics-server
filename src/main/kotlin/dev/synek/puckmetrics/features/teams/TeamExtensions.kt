package dev.synek.puckmetrics.features.teams

fun Team.toResponse() = TeamResponse(
    id = id,
    location = location,
    name = name,
)
