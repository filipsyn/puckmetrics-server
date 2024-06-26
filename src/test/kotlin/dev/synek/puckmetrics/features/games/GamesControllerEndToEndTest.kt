package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.common.BaseIntegrationTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc
@Sql("/test-data/teams.sql")
@Sql("/test-data/games.sql")
class GamesControllerEndToEndTest(
    @Autowired private val mockMvc: MockMvc
) : BaseIntegrationTest() {
    @Nested
    inner class GetAllGames {
        @Test
        fun `returns all games`() {
            // Act
            val response = mockMvc.get("/games")

            // Assert
            response.andExpect {
                status { isOk() }

                jsonPath("$[0].id") { value(1001) }
                jsonPath("$[0].homeTeamId") { value(200) }
                jsonPath("$[0].homeGoals") { value(4) }
                jsonPath("$[0].awayTeamId") { value(100) }
                jsonPath("$[0].awayGoals") { value(2) }

                jsonPath("$[1].id") { value(1002) }
                jsonPath("$[1].homeTeamId") { value(400) }
                jsonPath("$[1].homeGoals") { value(2) }
                jsonPath("$[1].awayTeamId") { value(300) }
                jsonPath("$[1].awayGoals") { value(3) }

                jsonPath("$[2].id") { value(1003) }
                jsonPath("$[2].homeTeamId") { value(600) }
                jsonPath("$[2].homeGoals") { value(5) }
                jsonPath("$[2].awayTeamId") { value(500) }
                jsonPath("$[2].awayGoals") { value(1) }

                jsonPath("$[3].id") { value(1004) }
                jsonPath("$[3].homeTeamId") { value(300) }
                jsonPath("$[3].homeGoals") { value(3) }
                jsonPath("$[3].awayTeamId") { value(100) }
                jsonPath("$[3].awayGoals") { value(2) }

                jsonPath("$[4].id") { value(1005) }
                jsonPath("$[4].homeTeamId") { value(500) }
                jsonPath("$[4].homeGoals") { value(1) }
                jsonPath("$[4].awayTeamId") { value(200) }
                jsonPath("$[4].awayGoals") { value(4) }
            }
        }

        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no games are present`() {
            // Act
            val response = mockMvc.get("/games")

            // Assert
            response.andExpect {
                status { isOk() }
                content { json("[]") }
            }
        }
    }

    @Nested
    inner class GetGameById {
        @Test
        fun `returns 404 when game is not found`() {
            // Arrange
            val invalidGameId = 999L

            // Act
            val result = mockMvc.get("/games/$invalidGameId")

            // Assert
            result.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `returns game by id`() {
            // Arrange
            val gameId = 1001L

            // Act
            val result = mockMvc.get("/games/$gameId")

            // Assert
            result.andExpect {
                status { isOk() }

                jsonPath("$.id") { value(1001) }
                jsonPath("$.homeTeam.id") { value(200) }
                jsonPath("$.homeTeam.location") { value("Chicago") }
                jsonPath("$.homeTeam.name") { value("Blackhawks") }
                jsonPath("$.homeGoals") { value(4) }
                jsonPath("$.awayTeam.id") { value(100) }
                jsonPath("$.awayTeam.location") { value("Boston") }
                jsonPath("$.awayTeam.name") { value("Bruins") }
                jsonPath("$.awayGoals") { value(2) }
                jsonPath("$.season") { value("20202021") }
                jsonPath("$.type") { value("R") }
                jsonPath("$.dateTimeUtc") { value("2020-10-20T00:00:00.000+00:00") }
                jsonPath("$.outcome") { value("home win REG") }
                jsonPath("$.venueName") { value("United Center") }
            }
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `returns 400 when season is missing`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when season is too short`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "2021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when season is too long`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "202120212",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when type is missing`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when type is too long`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "Regular",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when dateTimeUtc is missing`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when awayGoals is negative number`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": -2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when homeGoals is negative number`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": -4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when outcome is missing`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 400 when venue name is missing`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `returns 404 when home team is not found`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 999,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `returns 404 when away team is not foud`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 999,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        @Rollback
        fun `creates game`() {
            // Arrange
            val game = """
                {
                    "homeTeamId": 200,
                    "homeGoals": 4,
                    "awayTeamId": 100,
                    "awayGoals": 2,
                    "season": "20202021",
                    "type": "R",
                    "dateTimeUtc": "2020-10-20T00:00:00.000+00:00",
                    "outcome": "home win REG",
                    "venueName": "United Center"
                }
            """.trimIndent()

            // Act
            val result = mockMvc.post("/games") {
                contentType = MediaType.APPLICATION_JSON
                content = game
            }

            // Assert
            result.andExpect {
                status { isCreated() }
                header { exists("Location") }
                content { contentType("application/json") }

                jsonPath("$.id") { value(1) }
                jsonPath("$.homeTeam.id") { value(200) }
                jsonPath("$.homeTeam.location") { value("Chicago") }
                jsonPath("$.homeTeam.name") { value("Blackhawks") }
                jsonPath("$.homeGoals") { value(4) }
                jsonPath("$.awayTeam.id") { value(100) }
                jsonPath("$.awayTeam.location") { value("Boston") }
                jsonPath("$.awayTeam.name") { value("Bruins") }
                jsonPath("$.awayGoals") { value(2) }
                jsonPath("$.season") { value("20202021") }
                jsonPath("$.type") { value("R") }
                jsonPath("$.dateTimeUtc") { value("2020-10-20T00:00:00.000+00:00") }
                jsonPath("$.outcome") { value("home win REG") }
                jsonPath("$.venueName") { value("United Center") }
            }
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `returns 400 when game is not found`() {
            // Arrange
            val invalidGameId = 999L

            // Act
            val result = mockMvc.delete("/games/$invalidGameId")

            // Assert
            result.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        @Rollback
        fun `deletes game`() {
            // Arrange
            val gameId = 1001L

            // Act
            val result = mockMvc.delete("/games/$gameId")

            // Assert
            result.andExpect {
                status { isNoContent() }
            }
        }
    }
}
