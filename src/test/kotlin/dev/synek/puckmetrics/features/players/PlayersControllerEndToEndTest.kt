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
import org.springframework.test.web.servlet.*
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
                jsonPath("$.nationality") { value("CZE") }
                jsonPath("$.birthCity") { value("Kladno") }
                jsonPath("$.position") { value("RW") }
                jsonPath("$.birthDate") { value("1972-02-15T00:00:00.000+00:00") }
                jsonPath("$.height") { value("190") }
                jsonPath("$.weight") { value("230") }
                jsonPath("$.shoots") { value("L") }
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
                jsonPath("$.id") { value("1") }
                jsonPath("$.firstName") { value("Wayne") }
                jsonPath("$.lastName") { value("Gretzky") }
                jsonPath("$.nationality") { value("CAN") }
                jsonPath("$.birthCity") { value("Brantford") }
                jsonPath("$.position") { value("C") }
                jsonPath("$.birthDate") { value("1961-01-26T00:00:00.000+00:00") }
                jsonPath("$.height") { value("185") }
                jsonPath("$.weight") { value("185") }
                jsonPath("$.shoots") { value("L") }
            }
        }
    }

    @Nested
    inner class UpdatePlayer {
        @Test
        fun `responds with 404 when player with provided ID does not exist`() {
            // Arrange
            val invalidId = 999L
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
                    "shoots": "R"
                    }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$invalidId") {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `responds with 400 when first name is missing`() {
            // Arrange
            val playerId = 100L
            val request = """
            {
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "3000-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": ${Constants.Player.MINIMUM_HEIGHT - 1},
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": ${Constants.Player.MAXIMUM_HEIGHT + 1},
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": ${Constants.Player.MINIMUM_WEIGHT - 1},
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": ${Constants.Player.MAXIMUM_WEIGHT + 1}
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
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
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "RIGHT"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `updates a player`() {
            // Arrange
            val playerId = 100L
            val request = """
            {
                "firstName": "David",
                "lastName": "Pastrnak",
                "nationality": "CZE",
                "birthCity": "Havirov",
                "position": "C",
                "birthDate": "1996-05-25",
                "height": 182,
                "weight": 194,
                "shoots": "R"
            }
            """.trimIndent()

            // Act
            val response = mockMvc.put("$baseUrl/$playerId") {
                contentType = MediaType.APPLICATION_JSON
                content = request
            }

            // Assert
            response.andExpect {
                status { isAccepted() }
                content { contentType("application/json") }
                jsonPath("$.id") { value("100") }
                jsonPath("$.firstName") { value("David") }
                jsonPath("$.lastName") { value("Pastrnak") }
                jsonPath("$.nationality") { value("CZE") }
                jsonPath("$.birthCity") { value("Havirov") }
                jsonPath("$.position") { value("C") }
                jsonPath("$.birthDate") { value("1996-05-25T00:00:00.000+00:00") }
                jsonPath("$.height") { value("182") }
                jsonPath("$.weight") { value("194") }
                jsonPath("$.shoots") { value("R") }
            }
        }
    }

    @Nested
    inner class DeletePlayer {
        @Test
        fun `responds with 404 when player with provided ID does not exist`() {
            // Arrange
            val invalidId = 999L

            // Act
            val response = mockMvc.delete("$baseUrl/$invalidId")

            // Assert
            response.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        @Rollback
        fun `deletes a player`() {
            // Arrange
            val playerId = 100L

            // Act
            val response = mockMvc.delete("$baseUrl/$playerId")

            // Assert
            response.andExpect {
                status { isNoContent() }
            }
        }
    }
}
