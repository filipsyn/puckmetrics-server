package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.*
import dev.synek.puckmetrics.features.games.Game
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStats
import dev.synek.puckmetrics.features.games.stats.teams.GameTeamStatsRepository
import dev.synek.puckmetrics.features.games.stats.teams.PlayerSeasonFaceOffProjectionImpl
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

    @Nested
    inner class GetTopAssisters {
        @Test
        fun `throws exception when topPlayersPerSeason is less than 1`() {
            // Arrange
            val topPlayersPerSeason = 0

            // Act & Assert
            assertThrows<IllegalArgumentException> {
                statsService.getTopAssisters(topPlayersPerSeason)
            }
        }

        @Test
        fun `returns empty list when no players have recorded assists`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonAssistsProjection>()
            every { gameTeamStatsRepository.getPlayersAssistsPerSeason() } returns expectedResult

            // Act
            val result = statsService.getTopAssisters()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns top three assisters for each season`() {
            // Arrange
            val assisters = listOf(
                PlayerSeasonAssistsProjectionImpl(
                    playerId = 1001,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    totalAssists = 59,
                ),
                PlayerSeasonAssistsProjectionImpl(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Sidney",
                    lastName = "Crosby",
                    totalAssists = 70,
                ),
                PlayerSeasonAssistsProjectionImpl(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Joe",
                    lastName = "Thornton",
                    totalAssists = 80,
                ),
                PlayerSeasonAssistsProjectionImpl(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Nicklas",
                    lastName = "Backstrom",
                    totalAssists = 90,
                ),
            )

            every { gameTeamStatsRepository.getPlayersAssistsPerSeason() } returns assisters

            val expectedResult = listOf(
                PlayerSeasonAssistsResponse(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Nicklas",
                    lastName = "Backstrom",
                    totalAssists = 90,
                ),
                PlayerSeasonAssistsResponse(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Joe",
                    lastName = "Thornton",
                    totalAssists = 80,
                ),
                PlayerSeasonAssistsResponse(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Sidney",
                    lastName = "Crosby",
                    totalAssists = 70,
                ),
            )

            // Act
            val result = statsService.getTopAssisters()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.size).isEqualTo(expectedResult.size) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }
    }

    @Nested
    inner class GetTopPointScorers {
        @Test
        fun `throws exception when topPlayersPerSeason is less than 1`() {
            // Arrange
            val topPlayersPerSeason = 0

            // Act & Assert
            assertThrows<IllegalArgumentException> {
                statsService.getTopPointScorers(topPlayersPerSeason)
            }
        }

        @Test
        fun `returns empty list when no players have recorded points`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonPointsProjection>()
            every { gameTeamStatsRepository.getPlayersPointsPerSeason() } returns expectedResult

            // Act
            val result = statsService.getTopPointScorers()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns top three point scorers for each season`() {
            // Arrange
            val pointScorers = listOf(
                PlayerSeasonPointsProjectionImpl(
                    playerId = 1001,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    totalPoints = 109,
                ),
                PlayerSeasonPointsProjectionImpl(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Sidney",
                    lastName = "Crosby",
                    totalPoints = 120,
                ),
                PlayerSeasonPointsProjectionImpl(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Joe",
                    lastName = "Thornton",
                    totalPoints = 100,
                ),
                PlayerSeasonPointsProjectionImpl(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Nicklas",
                    lastName = "Backstrom",
                    totalPoints = 130,
                ),
            )

            every { gameTeamStatsRepository.getPlayersPointsPerSeason() } returns pointScorers

            val expectedResult = listOf(
                PlayerSeasonPointsResponse(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Nicklas",
                    lastName = "Backstrom",
                    totalPoints = 130,
                ),
                PlayerSeasonPointsResponse(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Sidney",
                    lastName = "Crosby",
                    totalPoints = 120,
                ),
                PlayerSeasonPointsResponse(
                    playerId = 1001,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    totalPoints = 109,
                ),
            )

            // Act
            val result = statsService.getTopPointScorers()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.size).isEqualTo(expectedResult.size) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }
    }

    @Nested
    inner class GetTopFaceOffTakers {
        @Test
        fun `throws exception when topPlayersPerSeason is less than 1`() {
            // Arrange
            val topPlayersPerSeason = 0

            // Act & Assert
            assertThrows<IllegalArgumentException> {
                statsService.getTopFaceOffTakers(topPlayersPerSeason)
            }
        }

        @Test
        fun `returns empty list when no players have recorded face offs`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonFaceOffProjection>()
            every { gameTeamStatsRepository.getPlayersFaceOffPercentagePerSeason() } returns expectedResult

            // Act
            val result = statsService.getTopFaceOffTakers()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns top three face off takers for each season`() {
            // Arrange
            val faceOffTakers = listOf(
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 1001,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    faceOffsTaken = 200,
                    faceOffWins = 50,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Sidney",
                    lastName = "Crosby",
                    faceOffsTaken = 200,
                    faceOffWins = 125,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Joe",
                    lastName = "Thornton",
                    faceOffsTaken = 200,
                    faceOffWins = 75,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 1004,
                    season = "20112012",
                    firstName = "Nicklas",
                    lastName = "Backstrom",
                    faceOffsTaken = 500,
                    faceOffWins = 125,
                ),
            )

            every { gameTeamStatsRepository.getPlayersFaceOffPercentagePerSeason() } returns faceOffTakers

            val expectedResult = listOf(
                PlayerSeasonFaceOffResponse(
                    playerId = 1002,
                    season = "20112012",
                    firstName = "Sidney",
                    lastName = "Crosby",
                    faceOffWinPercentage = 62.5,
                ),
                PlayerSeasonFaceOffResponse(
                    playerId = 1003,
                    season = "20112012",
                    firstName = "Joe",
                    lastName = "Thornton",
                    faceOffWinPercentage = 37.5,
                ),
                PlayerSeasonFaceOffResponse(
                    playerId = 1001,
                    season = "20112012",
                    firstName = "Evgeni",
                    lastName = "Malkin",
                    faceOffWinPercentage = 25.0,
                ),
            )

            // Act
            val result = statsService.getTopFaceOffTakers()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.size).isEqualTo(expectedResult.size) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }
    }
}
