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
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_COACHES}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns best coach in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_COACHES}")

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
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_GOAL_SCORERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns goal scored by each player in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_GOAL_SCORERS}")

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

                content { jsonPath("$[2].playerId") { value(2003) } }
                content { jsonPath("$[2].season") { value("19981999") } }
                content { jsonPath("$[2].firstName") { value("Wayne") } }
                content { jsonPath("$[2].lastName") { value("Gretzky") } }
                content { jsonPath("$[2].totalGoals") { value(3) } }

                content { jsonPath("$[3].playerId") { value(2004) } }
                content { jsonPath("$[3].season") { value("19981999") } }
                content { jsonPath("$[3].firstName") { value("Eric") } }
                content { jsonPath("$[3].lastName") { value("Lindros") } }
                content { jsonPath("$[3].totalGoals") { value(2) } }
            }
        }
    }

    @Nested
    inner class GetTopAssisters {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no players have assists`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_ASSISTERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns assists made by each player in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_ASSISTERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { jsonPath("$[0].playerId") { value(2001) } }
                content { jsonPath("$[0].season") { value("20112012") } }
                content { jsonPath("$[0].firstName") { value("Jaromir") } }
                content { jsonPath("$[0].lastName") { value("Jagr") } }
                content { jsonPath("$[0].totalAssists") { value(1) } }

                content { jsonPath("$[1].playerId") { value(2001) } }
                content { jsonPath("$[1].season") { value("19981999") } }
                content { jsonPath("$[1].firstName") { value("Jaromir") } }
                content { jsonPath("$[1].lastName") { value("Jagr") } }
                content { jsonPath("$[1].totalAssists") { value(6) } }

                content { jsonPath("$[2].playerId") { value(2002) } }
                content { jsonPath("$[2].season") { value("19981999") } }
                content { jsonPath("$[2].firstName") { value("Mario") } }
                content { jsonPath("$[2].lastName") { value("Lemieux") } }
                content { jsonPath("$[2].totalAssists") { value(3) } }

                content { jsonPath("$[3].playerId") { value(2004) } }
                content { jsonPath("$[3].season") { value("19981999") } }
                content { jsonPath("$[3].firstName") { value("Eric") } }
                content { jsonPath("$[3].lastName") { value("Lindros") } }
                content { jsonPath("$[3].totalAssists") { value(2) } }
            }
        }
    }

    @Nested
    inner class GetTopPointScorers {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no players have scored points`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_POINT_SCORERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns points scored by each player in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_POINT_SCORERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { jsonPath("$[0].playerId") { value(2001) } }
                content { jsonPath("$[0].season") { value("20112012") } }
                content { jsonPath("$[0].firstName") { value("Jaromir") } }
                content { jsonPath("$[0].lastName") { value("Jagr") } }
                content { jsonPath("$[0].totalPoints") { value(3) } }

                content { jsonPath("$[1].playerId") { value(2001) } }
                content { jsonPath("$[1].season") { value("19981999") } }
                content { jsonPath("$[1].firstName") { value("Jaromir") } }
                content { jsonPath("$[1].lastName") { value("Jagr") } }
                content { jsonPath("$[1].totalPoints") { value(11) } }

                content { jsonPath("$[2].playerId") { value(2004) } }
                content { jsonPath("$[2].season") { value("19981999") } }
                content { jsonPath("$[2].firstName") { value("Eric") } }
                content { jsonPath("$[2].lastName") { value("Lindros") } }
                content { jsonPath("$[2].totalPoints") { value(4) } }

                content { jsonPath("$[3].playerId") { value(2002) } }
                content { jsonPath("$[3].season") { value("19981999") } }
                content { jsonPath("$[3].firstName") { value("Mario") } }
                content { jsonPath("$[3].lastName") { value("Lemieux") } }
                content { jsonPath("$[3].totalPoints") { value(4) } }
            }
        }
    }

    @Nested
    inner class GetTopFaceOffTakers {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no players have taken face-offs`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_FACE_OFF_TAKERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }

        @Test
        fun `returns face-offs taken by each player in each season`() {
            // Act
            val result = mockMvc.get("$baseUrl/${StatsEndpointURLs.GET_TOP_FACE_OFF_TAKERS}")

            // Assert
            result.andExpect {
                status { isOk() }
                content { jsonPath("$[0].playerId") { value(2001) } }
                content { jsonPath("$[0].season") { value("20112012") } }
                content { jsonPath("$[0].firstName") { value("Jaromir") } }
                content { jsonPath("$[0].lastName") { value("Jagr") } }
                content { jsonPath("$[0].faceOffWinPercentage") { value(40.0) } }

                content { jsonPath("$[1].playerId") { value(2004) } }
                content { jsonPath("$[1].season") { value("19981999") } }
                content { jsonPath("$[1].firstName") { value("Eric") } }
                content { jsonPath("$[1].lastName") { value("Lindros") } }
                content { jsonPath("$[1].faceOffWinPercentage") { value(61.5) } }

                content { jsonPath("$[2].playerId") { value(2001) } }
                content { jsonPath("$[2].season") { value("19981999") } }
                content { jsonPath("$[2].firstName") { value("Jaromir") } }
                content { jsonPath("$[2].lastName") { value("Jagr") } }
                content { jsonPath("$[2].faceOffWinPercentage") { value(48.0) } }

                content { jsonPath("$[3].playerId") { value(2003) } }
                content { jsonPath("$[3].season") { value("19981999") } }
                content { jsonPath("$[3].firstName") { value("Wayne") } }
                content { jsonPath("$[3].lastName") { value("Gretzky") } }
                content { jsonPath("$[3].faceOffWinPercentage") { value(48.0) } }
            }
        }
    }
}
