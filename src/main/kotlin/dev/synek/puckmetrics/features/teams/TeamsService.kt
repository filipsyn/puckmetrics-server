package dev.synek.puckmetrics.features.teams

import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class TeamsService(
    private val teamsRepository: TeamsRepository,
) {
    fun get(): Iterable<Team> = teamsRepository.findAll()

    fun get(id: Long): Team? = teamsRepository.findById(id).getOrNull()

    fun create(team: Team): Team =
        teamsRepository.save(team)

    fun update(team: Team): Team? {
        val existingTeam = teamsRepository.findById(team.id).getOrNull()
            ?: return null

        val updatedTeam = existingTeam.copy(
            location = team.location,
            name = team.name,
            abbreviation = team.abbreviation
        )

        return teamsRepository.save(updatedTeam)
    }

    fun delete(id: Long): Boolean {
        val team = teamsRepository.findById(id).getOrNull()
            ?: return false

        teamsRepository.delete(team)
        return true
    }
}
