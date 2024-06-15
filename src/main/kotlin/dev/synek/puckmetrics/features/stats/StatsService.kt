package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.BestCoachInSeasonResponse
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
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
        .map { season ->
            BestCoachInSeasonResponse(
                season = season.key ?: "Unknown",
                coach = season.value?.key ?: "Unknown",
                wins = season.value?.value ?: 0,
            )
        }
}
