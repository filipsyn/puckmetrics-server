package dev.synek.puckmetrics.features.games.stats.teams

import org.springframework.data.jpa.repository.JpaRepository

interface GameTeamStatsRepository : JpaRepository<GameTeamStats, GameTeamStatsId>
