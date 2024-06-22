package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.BestCoachInSeasonResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonGoalsResponse
import dev.synek.puckmetrics.features.games.Game
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStats
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

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

    @Nested
    inner class GetTopGoalScorers {

        @Test
        fun `throws exception when topPlayersPerSeason is less than 1`() {
            // Arrange
            val topPlayersPerSeason = 0

            // Act & Assert
            assertThrows<IllegalArgumentException> {
                statsService.getTopGoalScorers(topPlayersPerSeason)
            }

        }

        @Test
        fun `returns empty list when no players have recorded goals`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonGoalsProjection>()
            every { gameTeamStatsRepository.getPlayersGoalsPerSeason() } returns expectedResult

            // Act
            val result = statsService.getTopGoalScorers()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns top three goal scorers for each season`() {
            // Arrange
            val goalScorers = listOf(
                PlayerSeasonGoalsProjectionImpl(
                    playerId = 1001,
                    season = "20112012",
                    firstName = "James",
                    lastName = "Neal",
                    totalGoals = 40,
                ),
                PlayerSeasonGoalsProjectionImpl(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    totalGoals = 50,
                ),
                PlayerSeasonGoalsProjectionImpl(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Marian",
                    lastName = "Gaborik",
                    totalGoals = 41,
                ),
                PlayerSeasonGoalsProjectionImpl(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Steven",
                    lastName = "Stamkos",
                    totalGoals = 60,
                ),
            )

            every { gameTeamStatsRepository.getPlayersGoalsPerSeason() } returns goalScorers

            val expectedResult = listOf(
                PlayerSeasonGoalsResponse(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Steven",
                    lastName = "Stamkos",
                    totalGoals = 60,
                ),
                PlayerSeasonGoalsResponse(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    totalGoals = 50,
                ),
                PlayerSeasonGoalsResponse(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Marian",
                    lastName = "Gaborik",
                    totalGoals = 41,
                ),
            )

            // Act
            val result = statsService.getTopGoalScorers()

            // Assert
            assertAll(
                { assertThat(result).isNotNull },
                { assertThat(result.size).isEqualTo(expectedResult.size) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }
    }
}
