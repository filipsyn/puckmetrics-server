package dev.synek.puckmetrics.features.teams

object TeamMapper {
    fun toResponse(team: Team): TeamResponse = TeamResponse(
        id = team.id,
        location = team.location,
        name = team.name,
    )
}
