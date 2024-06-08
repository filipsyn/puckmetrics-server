package dev.synek.puckmetrics.features.teams

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class TeamsServiceUnitTest {
    private val teamsRepository = mockk<TeamsRepository>()
    private val teamsService = TeamsService(teamsRepository)

    private val teams = listOf(
        Team(location = "New York", name = "Rangers"),
        Team(location = "Montreal", name = "Canadiens"),
        Team(location = "Chicago", name = "Blackhawks"),
        Team(location = "Detroit", name = "Red Wings"),
        Team(location = "Boston", name = "Bruins"),
        Team(location = "Toronto", name = "Maple Leafs"),
    )

    @Nested
    inner class GetAll {

        @Test
        fun `lists all teams when present`() {
            // Arrange
            every { teamsRepository.findAll() } returns teams

            // Act
            val result = teamsService.get()

            // Assert
            assertThat(result).isNotNull
            assertThat(result.count()).isEqualTo(teams.count())
            assertThat(result).isEqualTo(teams)
        }

        @Test
        fun `returns empty list when no teams are present`() {
            // Arrange
            every { teamsRepository.findAll() } returns emptyList()

            // Act
            val result = teamsService.get()

            // Assert
            assertThat(result).isNotNull
            assertThat(result.count()).isEqualTo(0)
            assertThat(result).isEqualTo(emptyList<Team>())
        }
    }

    @Nested
    inner class GetById {
        @Test
        fun `returns team when present`() {
            // Arrange
            val id = 1L
            val team = Team(id = id, location = "New York", name = "Rangers")
            every { teamsRepository.findById(id) } returns Optional.of(team)

            // Act
            val result = teamsService.get(id)

            // Assert
            assertThat(result).isNotNull
            assertThat(result).isEqualTo(team)
        }

        @Test
        fun `returns null when team is not present`() {
            // Arrange
            val id = 999L
            every { teamsRepository.findById(id) } returns Optional.empty()

            // Act
            val result = teamsService.get(id)

            // Assert
            assertThat(result).isNull()
        }
    }
}