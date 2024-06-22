package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.BestCoachInSeasonResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonAssistsResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonGoalsResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonPointsResponse
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_TOP_ASSISTERS_COUNT
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_TOP_SCORERS_COUNT
import org.springframework.stereotype.Service

@Service
class StatsService(
    private val gameTeamStatsRepository: GameTeamStatsRepository
) {

    fun getBestCoaches(): List<BestCoachInSeasonResponse> = gameTeamStatsRepository.findAll()
        .groupBy { gameStat -> gameStat.game?.season }
        .filterKeys { season -> season != null }
        .mapValues { season -> season.value.groupBy { it.headCoach } }
        .mapValues { season ->
            season.value.mapValues { coachGame -> coachGame.value.count { it.won == true } }
        }
        .mapValues { season -> season.value.maxByOrNull { gamesWonByCoach -> gamesWonByCoach.value } }
        .toSortedMap(compareBy { it })
        .map { season ->
            BestCoachInSeasonResponse(
                season = season.key ?: "Unknown",
                coach = season.value?.key ?: "Unknown",
                wins = season.value?.value ?: 0,
            )
        }

    fun getTopGoalScorers(topPlayersPerSeason: Int = DEFAULT_TOP_SCORERS_COUNT): List<PlayerSeasonGoalsResponse> {
        require(topPlayersPerSeason >= 1) { INVALID_COUNT_ERROR }

        return gameTeamStatsRepository.getPlayersGoalsPerSeason()
            .groupBy { it.season }
            .flatMap { (_, players) ->
                players.sortedByDescending { it.totalGoals }
                    .take(topPlayersPerSeason)
            }
            .sortedWith(compareByDescending<PlayerSeasonGoalsProjection> { it.season }
                .thenByDescending { it.totalGoals })
            .map(::toPlayerSeasonGoals)
    }

    fun getTopAssisters(topPlayersPerSeason: Int = DEFAULT_TOP_ASSISTERS_COUNT): List<PlayerSeasonAssistsResponse> {
        require(topPlayersPerSeason >= 1) { INVALID_COUNT_ERROR }

        return gameTeamStatsRepository.getPlayersAssistsPerSeason()
            .groupBy { it.season }
            .flatMap { (_, players) ->
                players.sortedByDescending { it.totalAssists }
                    .take(topPlayersPerSeason)
            }
            .sortedWith(compareByDescending<PlayerSeasonAssistsProjection> { it.season }
                .thenByDescending { it.totalAssists })
            .map(::toPlayerSeasonAssists)
    }

    fun getTopPointScorers(topPlayersPerSeason: Int = DEFAULT_TOP_SCORERS_COUNT): List<PlayerSeasonPointsResponse> {
        require(topPlayersPerSeason >= 1) { INVALID_COUNT_ERROR }

        return gameTeamStatsRepository.getPlayersPointsPerSeason()
            .groupBy { it.season }
            .flatMap { (_, players) ->
                players.sortedByDescending { it.totalPoints }
                    .take(topPlayersPerSeason)
            }
            .sortedWith(compareByDescending<PlayerSeasonPointsProjection> { it.season }
                .thenByDescending { it.totalPoints })
            .map(::toPlayerSeasonPoints)
    }


    private fun toPlayerSeasonGoals(player: PlayerSeasonGoalsProjection) =
        PlayerSeasonGoalsResponse(
            playerId = player.playerId,
            firstName = player.firstName,
            lastName = player.lastName,
            season = player.season,
            totalGoals = player.totalGoals,
        )

    private fun toPlayerSeasonAssists(player: PlayerSeasonAssistsProjection) =
        PlayerSeasonAssistsResponse(
            playerId = player.playerId,
            firstName = player.firstName,
            lastName = player.lastName,
            season = player.season,
            totalAssists = player.totalAssists,
        )

    private fun toPlayerSeasonPoints(player: PlayerSeasonPointsProjection) =
        PlayerSeasonPointsResponse(
            playerId = player.playerId,
            firstName = player.firstName,
            lastName = player.lastName,
            season = player.season,
            totalPoints = player.totalPoints,
        )

    companion object {
        private const val INVALID_COUNT_ERROR = "Must select at least one player per season."
    }
}
