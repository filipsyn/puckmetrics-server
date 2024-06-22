package dev.synek.puckmetrics.features.games.stats.teams

import dev.synek.puckmetrics.common.BaseIntegrationTest
import dev.synek.puckmetrics.contracts.PlayerSeasonAssistsResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonGoalsResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonPointsResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc
@Sql("/test-data/gameTeamStats.sql")
class GameTeamStatsRepositoryIntegrationTest(
    @Autowired private val gameTeamStatsRepository: GameTeamStatsRepository,
) : BaseIntegrationTest() {

    @Nested
    inner class GetPlayersGoalsPerSeason {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no players have scored goals`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonGoalsResponse>()

            // Act
            val result = gameTeamStatsRepository.getPlayersGoalsPerSeason()

            // Assert
            assertAll(
                { assertThat(result.size).isEqualTo(expectedResult.count()) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns goal scored by each player in each season`() {
            // Arrange
            val expectedResult = listOf(
                PlayerSeasonGoalsResponse(
                    playerId = 2001,
                    season = "20112012",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    totalGoals = 2,
                ),
                PlayerSeasonGoalsResponse(
                    playerId = 2001,
                    season = "19981999",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    totalGoals = 5,
                ),
                PlayerSeasonGoalsResponse(
                    playerId = 2003,
                    season = "19981999",
                    firstName = "Wayne",
                    lastName = "Gretzky",
                    totalGoals = 3,
                ),
                PlayerSeasonGoalsResponse(
                    playerId = 2004,
                    season = "19981999",
                    firstName = "Eric",
                    lastName = "Lindros",
                    totalGoals = 2,
                ),
                PlayerSeasonGoalsResponse(
                    playerId = 2002,
                    season = "19981999",
                    firstName = "Mario",
                    lastName = "Lemieux",
                    totalGoals = 1,
                )
            )

            // Act
            val result = gameTeamStatsRepository.getPlayersGoalsPerSeason()
            val mappedResult = result.map {
                PlayerSeasonGoalsResponse(
                    playerId = it.playerId,
                    season = it.season,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    totalGoals = it.totalGoals
                )
            }

            // Assert
            assertAll(
                { assertThat(mappedResult.size).isEqualTo(expectedResult.count()) },
                { assertThat(mappedResult).isEqualTo(expectedResult) },
            )
        }
    }

    @Nested
    inner class GetPlayerAssistsPerSeason {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no player has recorded assist`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonGoalsResponse>()

            // Act
            val result = gameTeamStatsRepository.getPlayersAssistsPerSeason()

            // Assert
            assertAll(
                { assertThat(result.size).isEqualTo(expectedResult.count()) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns assists recorded by each player in each season`() {
            // Arrange
            val expectedResult = listOf(
                PlayerSeasonAssistsResponse(
                    playerId = 2001,
                    season = "20112012",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    totalAssists = 1,
                ),
                PlayerSeasonAssistsResponse(
                    playerId = 2001,
                    season = "19981999",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    totalAssists = 6,
                ),
                PlayerSeasonAssistsResponse(
                    playerId = 2002,
                    season = "19981999",
                    firstName = "Mario",
                    lastName = "Lemieux",
                    totalAssists = 3,
                ),
                PlayerSeasonAssistsResponse(
                    playerId = 2004,
                    season = "19981999",
                    firstName = "Eric",
                    lastName = "Lindros",
                    totalAssists = 2,
                ),
                PlayerSeasonAssistsResponse(
                    playerId = 2003,
                    season = "19981999",
                    firstName = "Wayne",
                    lastName = "Gretzky",
                    totalAssists = 1,
                ),
            )

            // Act
            val result = gameTeamStatsRepository.getPlayersAssistsPerSeason()
            val mappedResult = result.map {
                PlayerSeasonAssistsResponse(
                    playerId = it.playerId,
                    season = it.season,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    totalAssists = it.totalAssists,
                )
            }

            // Assert
            assertAll(
                { assertThat(mappedResult.size).isEqualTo(expectedResult.count()) },
                { assertThat(mappedResult).isEqualTo(expectedResult) },
            )
        }
    }

    @Nested
    inner class GetPlayerPointsPerSeason {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no player has recorded points`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonGoalsResponse>()

            // Act
            val result = gameTeamStatsRepository.getPlayersPointsPerSeason()

            // Assert
            assertAll(
                { assertThat(result.size).isEqualTo(expectedResult.count()) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns points recorded by each player in each season`() {
            // Arrange
            val expectedResult = listOf(
                PlayerSeasonPointsResponse(
                    playerId = 2001,
                    season = "20112012",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    totalPoints = 3,
                ),
                PlayerSeasonPointsResponse(
                    playerId = 2001,
                    season = "19981999",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    totalPoints = 11,
                ),
                PlayerSeasonPointsResponse(
                    playerId = 2004,
                    season = "19981999",
                    firstName = "Eric",
                    lastName = "Lindros",
                    totalPoints = 4,
                ),
                PlayerSeasonPointsResponse(
                    playerId = 2002,
                    season = "19981999",
                    firstName = "Mario",
                    lastName = "Lemieux",
                    totalPoints = 4,
                ),
                PlayerSeasonPointsResponse(
                    playerId = 2003,
                    season = "19981999",
                    firstName = "Wayne",
                    lastName = "Gretzky",
                    totalPoints = 4,
                ),
            )

            // Act
            val result = gameTeamStatsRepository.getPlayersPointsPerSeason()
            val mappedResult = result.map {
                PlayerSeasonPointsResponse(
                    playerId = it.playerId,
                    season = it.season,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    totalPoints = it.totalPoints,
                )
            }

            // Assert
            assertAll(
                { assertThat(mappedResult.size).isEqualTo(expectedResult.count()) },
                { assertThat(mappedResult).isEqualTo(expectedResult) },
            )
        }
    }

    @Nested
    inner class GetPlayersFaceOffPercentagePerSeason {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no player has taken face offs`() {
            // Arrange
            val expectedResult = emptyList<PlayerSeasonGoalsResponse>()

            // Act
            val result = gameTeamStatsRepository.getPlayersFaceOffPercentagePerSeason()

            // Assert
            assertAll(
                { assertThat(result.size).isEqualTo(expectedResult.count()) },
                { assertThat(result).isEqualTo(expectedResult) },
            )
        }

        @Test
        fun `returns face off stats for each player by season`() {
            // Arrange
            val expectedResult = listOf(
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 2001,
                    season = "20112012",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    faceOffWins = 5,
                    faceOffsTaken = 10,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 2001,
                    season = "19981999",
                    firstName = "Jaromir",
                    lastName = "Jagr",
                    faceOffWins = 14,
                    faceOffsTaken = 20,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 2002,
                    season = "19981999",
                    firstName = "Mario",
                    lastName = "Lemieux",
                    faceOffWins = 5,
                    faceOffsTaken = 10,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 2004,
                    season = "19981999",
                    firstName = "Eric",
                    lastName = "Lindros",
                    faceOffWins = 4,
                    faceOffsTaken = 9,
                ),
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = 2003,
                    season = "19981999",
                    firstName = "Wayne",
                    lastName = "Gretzky",
                    faceOffWins = 4,
                    faceOffsTaken = 13,
                ),
            )

            // Act
            val result = gameTeamStatsRepository.getPlayersFaceOffPercentagePerSeason()
            val mappedResult = result.map {
                PlayerSeasonFaceOffProjectionImpl(
                    playerId = it.playerId,
                    season = it.season,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    faceOffWins = it.faceOffWins,
                    faceOffsTaken = it.faceOffsTaken,
                )
            }

            // Assert
            assertAll(
                { assertThat(mappedResult.size).isEqualTo(expectedResult.count()) },
                { assertThat(mappedResult).isEqualTo(expectedResult) },
            )
        }
    }
}

