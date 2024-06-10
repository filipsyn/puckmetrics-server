package dev.synek.puckmetrics.features.teams

import org.springframework.data.repository.CrudRepository

interface TeamsRepository : CrudRepository<Team, Long>
