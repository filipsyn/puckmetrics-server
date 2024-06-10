package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.common.BaseIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
class TeamsRepositoryIntegrationTest(
    @Autowired private val teamsRepository: TeamsRepository
) : BaseIntegrationTest() {
    private val teams = listOf(
        Team(id = 1, location = "New York", name = "Rangers"),
        Team(id = 2, location = "Montreal", name = "Canadiens"),
        Team(id = 3, location = "Chicago", name = "Blackhawks"),
        Team(id = 4, location = "Detroit", name = "Red Wings"),
        Team(id = 5, location = "Boston", name = "Bruins"),
        Team(id = 6, location = "Toronto", name = "Maple Leafs"),
    )

    @Nested
    inner class FindAll {
        init {
            teamsRepository.saveAll(teams)
        }

        @Test
        fun `returns all teams`() {
            // Act
            val result = teamsRepository.findAll()

            // Assert
            assertThat(result.count()).isEqualTo(teams.count())
            assertThat(result).isEqualTo(teams)
        }
    }

    @Nested
    inner class FindById {
        init {
            teamsRepository.saveAll(teams)
        }

        @Test
        fun `passing existing ID returns team`() {
            // Arrange
            val id = 1L

            // Act
            val result = teamsRepository.findById(id)

            // Assert
            assertThat(result.isPresent).isTrue()
            assertThat(result.get()).isEqualTo(teams.find { team -> team.id == id })
        }

        @Test
        fun `non-existing ID returns null`() {
            // Arrange
            val id = 999L

            // Act
            val result = teamsRepository.findById(id)

            // Assert
            assertThat(result.isPresent).isFalse()
        }
    }
}
