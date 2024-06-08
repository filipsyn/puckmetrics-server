package dev.synek.puckmetrics.features.teams

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.HttpStatus

class TeamsControllerUnitTest {
    private val teamsService = mockk<TeamsService>()
    private val teamsController = TeamsController(teamsService)

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
            every { teamsService.get() } returns teams
        }

        @Test
        fun `returns list of all teams`() {
            // Act
            val result = teamsController.getAllTeams()

            // Assert
            assertAll(
                { assertThat(result).isNotNull },
                { assertThat(result.statusCode).isEqualTo(HttpStatus.OK) },
                { assertThat(result.body).isEqualTo(teams) }
            )
        }
    }

    @Nested
    inner class GetTeamById {
        init {
            every { teamsService.get(1L) } returns teams.first()
            every { teamsService.get(999L) } returns null
        }

        @Test
        fun `responds 200 OK and with team info when present`() {
            // Act
            val response = teamsController.getTeamById(1L)

            // Assert
            assertAll(
                { assertThat(response).isNotNull },
                { assertThat(response.statusCode).isEqualTo(HttpStatus.OK) },
                { assertThat(response.body).isEqualTo(teams.first()) }
            )
        }

        @Test
        fun `responds 404 NOT FOUND when team is not found`() {
            // Act
            val response = teamsController.getTeamById(999L)

            // Assert
            assertAll(
                { assertThat(response).isNotNull },
                { assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND) },
            )
        }
    }
}
