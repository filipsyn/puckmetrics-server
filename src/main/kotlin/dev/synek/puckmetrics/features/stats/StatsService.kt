package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.BestCoachInSeasonResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonGoalsResponse
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
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
        require(topPlayersPerSeason >= 1) { "Must select at least one player per season." }

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

    private fun toPlayerSeasonGoals(player: PlayerSeasonGoalsProjection) =
        PlayerSeasonGoalsResponse(
            playerId = player.playerId,
            firstName = player.firstName,
            lastName = player.lastName,
            season = player.season,
            totalGoals = player.totalGoals,
        )
}
