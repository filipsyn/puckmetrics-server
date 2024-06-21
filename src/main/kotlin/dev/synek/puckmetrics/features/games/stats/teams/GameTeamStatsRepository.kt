package dev.synek.puckmetrics.features.games.stats.teams

import dev.synek.puckmetrics.features.stats.PlayerSeasonGoalsProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GameTeamStatsRepository : JpaRepository<GameTeamStats, GameTeamStatsId> {
    @Query(
        """
        SELECT player.id as playerId, player.firstName as firstName, player.lastName as lastName, game.season as season, SUM(gameSkaterStats.goals) as totalGoals
        FROM GameSkaterStats gameSkaterStats
        JOIN gameSkaterStats.player player
        JOIN gameSkaterStats.game game
        GROUP BY player.id, game.season, player.firstName, player.lastName
        ORDER BY game.season DESC, SUM(gameSkaterStats.goals) DESC
        """
    )
    fun getPlayersGoalsPerSeason(): List<PlayerSeasonGoalsProjection>
}

