package dev.synek.puckmetrics.features.teams

import org.springframework.stereotype.Service

@Service
class TeamsService(
    private val teamsRepository: TeamsRepository,
) {
    fun get(): Iterable<Team> = teamsRepository.findAll()

    fun get(id: Long): Team? {
        val team = teamsRepository.findById(id)

        return team.get()
    }
}

