package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.contracts.CreateUpdateTeamRequest
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus

class TeamsControllerUnitTest {
    private val teamsService = mockk<TeamsService>()
    private val teamsController = TeamsController(teamsService)
    private val pageable = mockk<Pageable>()

    private val teams = listOf(
        Team(location = "New York", name = "Rangers"),
        Team(location = "Montreal", name = "Canadiens"),
        Team(location = "Chicago", name = "Blackhawks"),
        Team(location = "Detroit", name = "Red Wings"),
        Team(location = "Boston", name = "Bruins"),
        Team(location = "Toronto", name = "Maple Leafs"),
    )

    @Nested
    inner class GetAllTeams {
        init {
            every { teamsService.get(pageable = any()) } returns teams
        }

        @Test
        fun `returns list of all teams`() {
            // Arrange
            val expectedTeams = teams.map(Team::toResponse)

            // Act
            val result = teamsController.getAllTeams(pageable)

            // Assert
            assertAll(
                { assertThat(result).isNotNull },
                { assertThat(result.statusCode).isEqualTo(HttpStatus.OK) },
                { assertThat(result.body).isEqualTo(expectedTeams) }
            )
        }
    }

    @Nested
    inner class GetTeamById {
        private val validTeamId = 1L
        private val invalidTeamId = 999L

        init {
            every { teamsService.get(validTeamId) } returns teams.first()
            every { teamsService.get(invalidTeamId) } returns null
        }

        @Test
        fun `responds 200 OK and with team info when present`() {
            // Arrange
            val expectedTeam = teams.first().toResponse()

            // Act
            val response = teamsController.getTeamById(validTeamId)

            // Assert
            assertAll(
                { assertThat(response).isNotNull },
                { assertThat(response.statusCode).isEqualTo(HttpStatus.OK) },
                { assertThat(response.body).isEqualTo(expectedTeam) }
            )
        }

        @Test
        fun `responds 404 NOT FOUND when team is not found`() {
            // Act
            val response = teamsController.getTeamById(invalidTeamId)

            // Assert
            assertAll(
                { assertThat(response).isNotNull },
                { assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND) },
            )
        }
    }

    @Nested
    inner class CreateTeam {
        init {
            every { teamsService.create(any()) } returns teams.first()
        }

        @Test
        fun `responds 201 CREATED and with team info when created`() {
            // Arrange
            val request = CreateUpdateTeamRequest(
                location = "New York",
                name = "Rangers",
                abbreviation = "NYR",
                franchiseId = null
            )
            val expectedTeam = teams.first().toResponse()

            // Act
            val response = teamsController.createTeam(request)

            // Assert
            assertAll(
                { assertThat(response).isNotNull },
                { assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED) },
                { assertThat(response.body).isEqualTo(expectedTeam) },
            )
        }
    }
}
