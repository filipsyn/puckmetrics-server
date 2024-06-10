package dev.synek.puckmetrics.features.teams

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@AutoConfigureMockMvc
@Sql("/test-data/teams.sql")
class TeamsControllerEndToEndTest(
    @Autowired private val mockMvc: MockMvc
) {
    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>(
            DockerImageName.parse("postgres:16")
        ).apply {
            withDatabaseName("integration-tests-db")
            withUsername("test-user")
            withPassword("test-password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
        }
    }

    private val baseUrl = "/teams"

    @Nested
    inner class GetAllTeams {
        @Test
        @Sql("/test-data/empty.sql")
        fun `empty list when no teams are present`() {
            mockMvc.get(baseUrl)
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    content { json("[]") }
                }
        }

        @Test
        fun `list of all teams`() {
            mockMvc.get(baseUrl)
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$[0].location") { value("Boston") }
                    jsonPath("$[0].name") { value("Bruins") }
                    jsonPath("$[1].location") { value("Chicago") }
                    jsonPath("$[1].name") { value("Blackhawks") }
                    jsonPath("$[2].location") { value("Detroit") }
                    jsonPath("$[2].name") { value("Red Wings") }
                    jsonPath("$[3].location") { value("Montreal") }
                    jsonPath("$[3].name") { value("Canadiens") }
                    jsonPath("$[4].location") { value("New York") }
                    jsonPath("$[4].name") { value("Rangers") }
                    jsonPath("$[5].location") { value("Toronto") }
                    jsonPath("$[5].name") { value("Maple Leafs") }
                }
        }
    }

    @Nested
    inner class GetTeamById {
        @Test
        fun `returns 404 when not found`() {
            // Arrange
            val invalidId = 999L

            // Act & Assert
            mockMvc.get("$baseUrl/$invalidId")
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `returns team when found`() {
            // Arrange
            val teamId = 100L

            // Act & Assert
            mockMvc.get("$baseUrl/$teamId")
                .andExpect {
                    status { isOk() }
                    content { contentType("application/json") }
                    jsonPath("$.location") { value("Boston") }
                    jsonPath("$.name") { value("Bruins") }
                }
        }
    }

    @Nested
    inner class CreateTeam {
        @Test
        fun `fails when name is missing`() {
            // Arrange
            val requestBody =
                """
                {
                    "location": "New York",
                    "abbreviation": "NYR"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `fails when location is missing`() {
            // Arrange
            val requestBody =
                """
                {
                    "name": "Rangers",
                    "abbreviation": "NYR"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `fails when abbreviation is missing`() {
            // Arrange
            val requestBody =
                """
                {
                    "location": "New York",
                    "name": "Rangers"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `creates a new team`() {
            // Arrange
            val requestBody =
                """
                {
                    "location": "New York",
                    "name": "Rangers",
                    "abbreviation": "NYR"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isCreated() }
                header { exists("Location") }
                content { contentType("application/json") }
                jsonPath("$.location") { value("New York") }
                jsonPath("$.name") { value("Rangers") }
            }
        }
    }

    @Nested
    inner class UpdateTeam {
        @Test
        fun `fails when passed invalid team ID`() {
            // Arrange
            val invalidId = 999L
            val requestBody =
                """
                {
                    "location": "New York",
                    "name": "Rangers",
                    "abbreviation": "NYR"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.put("$baseUrl/$invalidId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `fails when name is missing`() {
            // Arrange
            val teamId = 100L
            val requestBody =
                """
                {
                    "location": "New York",
                    "abbreviation": "NYR"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.put("$baseUrl/$teamId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `fails when location is missing`() {
            // Arrange
            val teamId = 100L
            val requestBody =
                """
                {
                    "name": "Rangers",
                    "abbreviation": "NYR"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.put("$baseUrl/$teamId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `fails when abbreviation is missing`() {
            // Arrange
            val teamId = 100L
            val requestBody =
                """
                {
                    "location": "New York",
                    "name": "Rangers"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.put("$baseUrl/$teamId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `updates an existing team`() {
            // Arrange
            val teamId = 100L
            val requestBody =
                """
                {
                    "location": "Nuevo York",
                    "name": "El Rangers",
                    "abbreviation": "RAN"
                }
                """.trimIndent()

            // Act
            val result = mockMvc.put("$baseUrl/$teamId") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }

            // Assert
            result.andExpect {
                status { isAccepted() }
                content { contentType("application/json") }
                jsonPath("$.location") { value("Nuevo York") }
                jsonPath("$.name") { value("El Rangers") }
            }
        }
    }

    @Nested
    inner class DeleteTeam {
        @Test
        fun `fails when passed invalid team ID`() {
            // Arrange
            val invalidId = 999L

            // Act
            val result = mockMvc.delete("$baseUrl/$invalidId")

            // Assert
            result.andExpect {
                status { isNotFound() }
            }
        }

        @Test
        fun `deletes an existing team`() {
            // Arrange
            val teamId = 100L

            // Act
            val result = mockMvc.delete("$baseUrl/$teamId")

            // Assert
            result.andExpect {
                status { isNoContent() }
            }
        }
    }
}
