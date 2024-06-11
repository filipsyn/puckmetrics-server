package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.common.BaseIntegrationTest
import dev.synek.puckmetrics.shared.Constants
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
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

    @Nested
    inner class CreatePlayer {
        @Test
        fun `responds with 400 when first name is missing`() {
            // Arrange
            val request = """
                {
                    "lastName": "Gretzky",
                    "nationality": "CAN",
                    "birthCity": "Brantford",
                    "position": "C",
                    "birthDate": "1961-01-26",
                    "height": 185,
                    "weight": 185,
                    "shoots": "L"
                    }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when last name is missing`() {
            // Arrange
            val request = """
                {
                    "firstName": "Wayne",
                    "nationality": "CAN",
                    "birthCity": "Brantford",
                    "position": "C",
                    "birthDate": "1961-01-26",
                    "height": 185,
                    "weight": 185,
                    "shoots": "L"
                    }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when nationality is missing`() {
            // Arrange
            val request = """
                {
                    "firstName": "Wayne",
                    "lastName": "Gretzky",
                    "birthCity": "Brantford",
                    "position": "C",
                    "birthDate": "1961-01-26",
                    "height": 185,
                    "weight": 185,
                    "shoots": "L"
                    }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when birth city is missing`() {
            // Arrange
            val request = """
                {
                    "firstName": "Wayne",
                    "lastName": "Gretzky",
                    "nationality: "CAN",
                    "position": "C",
                    "birthDate": "1961-01-26",
                    "height": 185,
                    "weight": 185,
                    "shoots": "L"
                }
                """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when position is missing`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "birthDate": "1961-01-26",
                "height": 185,
                "weight": 185,
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when birth date is missing`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "height": 185,
                "weight": 185,
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }


            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when birth date is in the future`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "3000-01-26",
                "height": 185,
                "weight": 185,
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when height is too low`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": ${Constants.Player.MINIMUM_HEIGHT - 1}
                "weight": 185,
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when height is too high`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": ${Constants.Player.MAXIMUM_HEIGHT + 1},
                "weight": 185,
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when weight is too low`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": 185,
                "weight": ${Constants.Player.MINIMUM_WEIGHT - 1},
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when weight is too high`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": 185,
                "weight": ${Constants.Player.MAXIMUM_WEIGHT + 1},
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when information about dominant hand is missing`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": 185,
                "weight": 185,
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `responds with 400 when information about dominant hand is too long`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": 185,
                "weight": 185,
                "shoots": "LEFT"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        @Rollback
        fun `creates a player`() {
            // Arrange
            val request = """
            {
                "firstName": "Wayne",
                "lastName": "Gretzky",
                "nationality": "CAN",
                "birthCity": "Brantford",
                "position": "C",
                "birthDate": "1961-01-26",
                "height": 185,
                "weight": 185,
                "shoots": "L"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isOk() }
                content { contentType("application/json") }
                jsonPath("$.firstName") { value("Wayne") }
                jsonPath("$.lastName") { value("Gretzky") }
            }
        }
    }
}
