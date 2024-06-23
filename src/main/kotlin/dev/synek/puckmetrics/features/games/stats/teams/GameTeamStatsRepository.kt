package dev.synek.puckmetrics.features.games.stats.teams

import dev.synek.puckmetrics.features.stats.PlayerSeasonAssistsProjection
import dev.synek.puckmetrics.features.stats.PlayerSeasonFaceOffProjection
import dev.synek.puckmetrics.features.stats.PlayerSeasonGoalsProjection
import dev.synek.puckmetrics.features.stats.PlayerSeasonPointsProjection
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_MINIMUM_FACE_OFFS_TAKEN
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_TOP_FACE_OFF_TAKERS_COUNT
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

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

    @Query(
        """
        SELECT player.id as playerId, player.firstName as firstName, player.lastName as lastName, game.season as season, SUM(gameSkaterStats.assists) as totalAssists
        FROM GameSkaterStats gameSkaterStats
        JOIN gameSkaterStats.player player
        JOIN gameSkaterStats.game game
        GROUP BY player.id, game.season, player.firstName, player.lastName
        ORDER BY game.season DESC, SUM(gameSkaterStats.assists) DESC
        """
    )
    fun getPlayersAssistsPerSeason(): List<PlayerSeasonAssistsProjection>


    @Query(
        """
        SELECT player.id as playerId, player.firstName as firstName, player.lastName as lastName, game.season as season, SUM(gameSkaterStats.goals + gameSkaterStats.assists) as totalPoints
        FROM GameSkaterStats gameSkaterStats
        JOIN gameSkaterStats.player player
        JOIN gameSkaterStats.game game
        GROUP BY playerId, season, firstName, lastName
        ORDER BY season DESC, totalPoints DESC
        """
    )
    fun getPlayersPointsPerSeason(): List<PlayerSeasonPointsProjection>

    @Query(
        """
        SELECT ranked.playerId as playerId, ranked.firstName as firstName, ranked.lastName as lastName, ranked.season as season, ranked.faceOffWins as faceOffWins,ranked.faceOffsTaken as faceOffsTaken
        FROM (
            SELECT player.id AS playerId, player.firstName AS firstName, player.lastName AS lastName, game.season AS season, 
                   COALESCE( SUM(gameSkaterStats.faceOffWins),0) AS faceOffWins, 
                   COALESCE(SUM(gameSkaterStats.faceOffsTaken),0) AS faceOffsTaken,
                   RANK() OVER (PARTITION BY game.season ORDER BY SUM(gameSkaterStats.faceOffWins) DESC) AS rank
            FROM GameSkaterStats gameSkaterStats
            JOIN gameSkaterStats.player player
            JOIN gameSkaterStats.game game
            GROUP BY player.id, game.season, player.firstName, player.lastName
            HAVING COALESCE(SUM(gameSkaterStats.faceOffsTaken),0) >= :faceOffsTakenThreshold
        ) AS ranked
        WHERE ranked.rank <= :topPlayersPerSeason
        ORDER BY ranked.season DESC, ranked.faceOffWins DESC
        """
    )
    fun getPlayersFaceOffPercentagePerSeason(
        @Param("topPlayersPerSeason") topPlayersPerSeason: Int = DEFAULT_TOP_FACE_OFF_TAKERS_COUNT,
        @Param("faceOffsTakenThreshold") faceOffsTakenThreshold: Int = DEFAULT_MINIMUM_FACE_OFFS_TAKEN
    ): List<PlayerSeasonFaceOffProjection>
}

