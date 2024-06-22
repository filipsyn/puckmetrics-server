package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.BestCoachInSeasonResponse
import dev.synek.puckmetrics.features.games.Game
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStats
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class StatsServiceUnitTest {
    private val gameTeamStatsRepository = mockk<GameTeamStatsRepository>()
    private val statsService = StatsService(gameTeamStatsRepository)

    @Nested
    inner class GetBestCoaches {
        @Test
        fun `returns empty list when no coaches are present`() {
            // Arrange
            val gameStats = emptyList<GameTeamStats>()
            every { gameTeamStatsRepository.findAll() } returns gameStats

            // Act
            val result = statsService.getBestCoaches()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
            )
        }

        @Test
        fun `returns best coach for each season`() {
            // Arrange
            val gameStats = listOf(
                GameTeamStats(headCoach = "Peter DeBoer", game = Game(season = "20122013"), won = false),
                GameTeamStats(headCoach = "Joel Quenneville", game = Game(season = "20122013"), won = true),
                GameTeamStats(headCoach = "John Tortorella", game = Game(season = "20122013"), won = true),
                GameTeamStats(headCoach = "Peter DeBoer", game = Game(season = "20122013"), won = true),
                GameTeamStats(headCoach = "Joel Quenneville", game = Game(season = "20122013"), won = true),
                GameTeamStats(headCoach = "John Tortorella", game = Game(season = "20122013"), won = false),
                GameTeamStats(headCoach = "Peter DeBoer", game = Game(season = "20122013"), won = false),
                GameTeamStats(headCoach = "Joel Quenneville", game = Game(season = "20122013"), won = true),
                GameTeamStats(headCoach = "John Tortorella", game = Game(season = "20122013"), won = false),

                GameTeamStats(headCoach = "Peter DeBoer", game = Game(season = "20132014"), won = false),
                GameTeamStats(headCoach = "Joel Quenneville", game = Game(season = "20132014"), won = false),
                GameTeamStats(headCoach = "John Tortorella", game = Game(season = "20132014"), won = true),
                GameTeamStats(headCoach = "Peter DeBoer", game = Game(season = "20132014"), won = true),
                GameTeamStats(headCoach = "Joel Quenneville", game = Game(season = "20132014"), won = true),
                GameTeamStats(headCoach = "John Tortorella", game = Game(season = "20132014"), won = false),
                GameTeamStats(headCoach = "Peter DeBoer", game = Game(season = "20132014"), won = false),
                GameTeamStats(headCoach = "Joel Quenneville", game = Game(season = "20132014"), won = false),
                GameTeamStats(headCoach = "John Tortorella", game = Game(season = "20132014"), won = true),
            )

            every { gameTeamStatsRepository.findAll() } returns gameStats

            // Act
            val result = statsService.getBestCoaches()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.size).isEqualTo(2) },
                {
                    assertThat(result).isEqualTo(
                        listOf(
                            BestCoachInSeasonResponse(season = "20132014", coach = "John Tortorella", wins = 2),
                            BestCoachInSeasonResponse(season = "20122013", coach = "Joel Quenneville", wins = 3),
                        )
                    )
                },
            )
        }
    }
}
