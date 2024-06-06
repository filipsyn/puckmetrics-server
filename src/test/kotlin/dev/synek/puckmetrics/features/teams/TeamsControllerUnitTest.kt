package dev.synek.puckmetrics.features.teams

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TeamsControllerUnitTest {
    @Nested
    inner class GetAllTeams {
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

        init {
            every { teamsService.get() } returns teams
        }

        @Test
        fun `returns list of all teams`() {
            // Act
            val result = teamsController.getAllTeams()

            // Assert
            Assertions.assertEquals(result.count(), teams.count())
        }
    }
}
