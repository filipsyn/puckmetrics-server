package dev.synek.puckmetrics.features.teams

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class TeamsService(
    private val teamsRepository: TeamsRepository,
) {
    fun get(pageable: Pageable): List<Team> =
        teamsRepository.findAll(pageable).toList()

    fun get(id: Long): Team? = teamsRepository.findById(id).getOrNull()

    fun create(team: Team): Team =
        teamsRepository.save(team)

    fun update(id: Long, team: Team): Team? {
        val existingTeam = teamsRepository.findById(id).getOrNull()
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
