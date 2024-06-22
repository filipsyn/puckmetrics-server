package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.common.BaseIntegrationTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc
@Sql("/test-data/gameTeamStats.sql")
class StatsControllerIntegrationTest(
    @Autowired private val mockMvc: MockMvc,
) : BaseIntegrationTest() {
    private val baseUrl = "/${StatsEndpointURLs.CONTROLLER_ROOT}"

    @Nested
    inner class GetBestCoaches {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no coaches are present`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.BEST_COACHES}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns best coach in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.BEST_COACHES}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { jsonPath("$[0].coach") { value("Mike Sullivan") } }
                content { jsonPath("$[0].season") { value("20112012") } }
                content { jsonPath("$[0].wins") { value(1) } }

                content { jsonPath("$[1].coach") { value("Mike Sullivan") } }
                content { jsonPath("$[1].season") { value("19981999") } }
                content { jsonPath("$[1].wins") { value(2) } }
            }
        }
    }

    @Nested
    inner class GetTopGoalScorers {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no players have scored goals`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.TOP_GOAL_SCORERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns goal scored by each player in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.TOP_GOAL_SCORERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { jsonPath("$[0].playerId") { value(2001) } }
                content { jsonPath("$[0].season") { value("20112012") } }
                content { jsonPath("$[0].firstName") { value("Jaromir") } }
                content { jsonPath("$[0].lastName") { value("Jagr") } }
                content { jsonPath("$[0].totalGoals") { value(2) } }

                content { jsonPath("$[1].playerId") { value(2001) } }
                content { jsonPath("$[1].season") { value("19981999") } }
                content { jsonPath("$[1].firstName") { value("Jaromir") } }
                content { jsonPath("$[1].lastName") { value("Jagr") } }
                content { jsonPath("$[1].totalGoals") { value(5) } }
            }
        }
    }
}
