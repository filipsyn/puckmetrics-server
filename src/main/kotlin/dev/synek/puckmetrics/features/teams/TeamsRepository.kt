package dev.synek.puckmetrics.features.teams

import org.springframework.data.jpa.repository.JpaRepository

interface TeamsRepository : JpaRepository<Team, Long>
