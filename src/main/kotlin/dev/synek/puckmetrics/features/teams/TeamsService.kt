package dev.synek.puckmetrics.features.teams

import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class TeamsService(
    private val teamsRepository: TeamsRepository,
) {
    fun get(): Iterable<Team> = teamsRepository.findAll()

    fun get(id: Long): Team? = teamsRepository.findById(id).getOrNull()
}

