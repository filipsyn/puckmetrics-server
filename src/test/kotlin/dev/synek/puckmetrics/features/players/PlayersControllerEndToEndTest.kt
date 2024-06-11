package dev.synek.puckmetrics.features.players

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
@Sql("/test-data/players.sql")
class PlayersControllerEndToEndTest(
    @Autowired private val mockMvc: MockMvc
) : BaseIntegrationTest() {
    private val baseUrl = "/players"

    @Nested
    inner class GetAllPlayers {
        @Test
        @Sql("/test-data/empty.sql")
        fun `returns empty list when no players are present`() {
            // Act
            val response = mockMvc.get(baseUrl)

            // Assert
            response.andExpect {
                status { isOk() }
                content { contentType("application/json") }
                content { json("[]") }
            }
        }

        @Test
        fun `lists all players`() {
            // Act
            val response = mockMvc.get(baseUrl)

            // Assert
            response.andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$[0].firstName") { value("Jaromir") }
                jsonPath("$[0].lastName") { value("Jagr") }
                jsonPath("$[1].firstName") { value("Wayne") }
                jsonPath("$[1].lastName") { value("Gretzky") }
                jsonPath("$[2].firstName") { value("Mario") }
                jsonPath("$[2].lastName") { value("Lemieux") }
                jsonPath("$[3].firstName") { value("Bobby") }
                jsonPath("$[3].lastName") { value("Orr") }
                jsonPath("$[4].firstName") { value("Gordie") }
                jsonPath("$[4].lastName") { value("Howe") }
            }
        }
    }

    @Nested
    inner class GetPlayerById {
        @Test
        fun `returns 404 when player with provided ID does not exist`() {
            // Arrange
            val invalidId = 999L

            // Act
            val response = mockMvc.get("$baseUrl/$invalidId")

            // Assert
            response.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `gets a player`() {
            // Arrange
            val playerId = 100L

            // Act
            val response = mockMvc.get("$baseUrl/$playerId")

            // Assert
            response.andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.firstName") { value("Jaromir") }
                jsonPath("$.lastName") { value("Jagr") }
            }
        }
    }
}
