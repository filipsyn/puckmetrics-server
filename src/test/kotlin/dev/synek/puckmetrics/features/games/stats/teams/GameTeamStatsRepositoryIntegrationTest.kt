package dev.synek.puckmetrics.features.games.stats.teams

import dev.synek.puckmetrics.common.BaseIntegrationTest
import dev.synek.puckmetrics.contracts.PlayerSeasonGoalsResponse
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
}

