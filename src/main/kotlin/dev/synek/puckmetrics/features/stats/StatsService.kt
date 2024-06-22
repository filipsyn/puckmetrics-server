package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.*
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_TOP_ASSISTERS_COUNT
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_TOP_FACE_OFF_TAKERS_COUNT
import dev.synek.puckmetrics.shared.Constants.Player.DEFAULT_TOP_SCORERS_COUNT
import org.springframework.stereotype.Service
import java.math.RoundingMode

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
        .sortedWith(compareByDescending { it.season })

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

    fun getTopFaceOffTakers(
        topPlayersPerSeason: Int = DEFAULT_TOP_FACE_OFF_TAKERS_COUNT
    ): List<PlayerSeasonFaceOffResponse> {
        require(topPlayersPerSeason >= 1) { INVALID_COUNT_ERROR }

        return gameTeamStatsRepository.getPlayersFaceOffPercentagePerSeason()
            .groupBy { it.season }
            .flatMap { (_, players) ->
                players.sortedByDescending { projection ->
                    if (projection.faceOffsTaken > 0) {
                        (projection.faceOffWins.toDouble() / projection.faceOffsTaken.toDouble()) * 100
                    } else {
                        0.0
                    }
                }.take(topPlayersPerSeason)
            }
            .map(::toPlayerSeasonFaceOff)
            .sortedWith(compareByDescending<PlayerSeasonFaceOffResponse> { it.season }
                .thenByDescending { it.faceOffWinPercentage })
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

    private fun toPlayerSeasonFaceOff(player: PlayerSeasonFaceOffProjection): PlayerSeasonFaceOffResponse {
        val faceOffWinPercentage = if (player.faceOffsTaken > 0) {
            (player.faceOffWins.toDouble() / player.faceOffsTaken.toDouble()) * 100
        } else {
            0.0
        }

        return PlayerSeasonFaceOffResponse(
            playerId = player.playerId,
            firstName = player.firstName,
            lastName = player.lastName,
            season = player.season,
            faceOffWinPercentage = faceOffWinPercentage.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
        )
    }

    companion object {
        private const val INVALID_COUNT_ERROR = "Must select at least one player per season."
    }
}
