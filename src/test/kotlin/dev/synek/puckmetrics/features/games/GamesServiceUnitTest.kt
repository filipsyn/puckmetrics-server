package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.features.teams.Team
import dev.synek.puckmetrics.features.teams.TeamsRepository
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class GamesServiceUnitTest {
    private val gamesRepository = mockk<GamesRepository>()
    private val teamsRepository = mockk<TeamsRepository>()
    private val gamesService = GamesService(gamesRepository, teamsRepository)
    private val pageable = mockk<Pageable>()

    val games = listOf(
        Game(id = 1L, homeTeamId = 1, awayTeamId = 2, homeGoals = 3, awayGoals = 2),
        Game(id = 2L, homeTeamId = 3, awayTeamId = 4, homeGoals = 2, awayGoals = 1),
        Game(id = 3L, homeTeamId = 5, awayTeamId = 6, homeGoals = 4, awayGoals = 3),
        Game(id = 4L, homeTeamId = 7, awayTeamId = 8, homeGoals = 5, awayGoals = 4),
        Game(id = 5L, homeTeamId = 9, awayTeamId = 10, homeGoals = 6, awayGoals = 5),
    )

    @Nested
    inner class GetAll {
        @Test
        fun `lists all games when present`() {
            // Arrange
            every { gamesRepository.findAll(pageable) } returns PageImpl(games)

            // Act
            val result = gamesService.get(pageable)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.count()).isEqualTo(games.count()) },
                { assertThat(result).isEqualTo(games) },
            )
        }

        @Test
        fun `returns empty list when no games are present`() {
            // Arrange
            every { gamesRepository.findAll(pageable) } returns PageImpl(emptyList())

            // Act
            val result = gamesService.get(pageable)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.count()).isEqualTo(0) },
                { assertThat(result).isEqualTo(emptyList<Game>()) },
            )
        }
    }

    @Nested
    inner class GetById {
        @Test
        fun `returns null when game not found`() {
            // Arrange
            val invalidGameId = 999L
            every { gamesRepository.findById(invalidGameId) } returns Optional.empty()

            // Act
            val result = gamesService.get(invalidGameId)

            // Assert
            assertThat(result).isNull()
        }

        @Test
        fun `returns game by id`() {
            // Arrange
            val gameId = 1L
            val game = Game(id = gameId, homeTeamId = 1, awayTeamId = 2, homeGoals = 3, awayGoals = 2)
            every { gamesRepository.findById(gameId) } returns Optional.of(game)

            // Act
            val result = gamesService.get(gameId)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(game) },
            )
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Create {
        private val teamId1 = 1L
        private val team1 = Team(
            id = teamId1,
            name = "Rangers",
            location = "New York"
        )

        private val teamId2 = 2L
        private val team2 = Team(
            id = teamId2,
            name = "Canadiens",
            location = "Montreal"
        )

        private val invalidTeamId = 999L

        @BeforeAll
        fun beforeAll() {
            every { teamsRepository.findById(teamId1) } returns Optional.of(team1)
            every { teamsRepository.findById(teamId2) } returns Optional.of(team2)
            every { teamsRepository.findById(invalidTeamId) } returns Optional.empty()
        }

        @Test
        fun `invalid home team id throws exception`() {
            // Arrange
            val game = Game(
                id = 6L,
                homeTeamId = invalidTeamId,
                awayTeamId = teamId2,
                homeGoals = 3,
                awayGoals = 2
            )

            // Act & Assert
            assertThrows<IllegalArgumentException> { gamesService.create(game) }
        }

        @Test
        fun `invalid away team id throws exception`() {
            // Arrange
            val game = Game(
                id = 6L,
                homeTeamId = teamId1,
                awayTeamId = invalidTeamId,
                homeGoals = 3,
                awayGoals = 2
            )

            // Act & Assert
            assertThrows<IllegalArgumentException> { gamesService.create(game) }
        }

        @Test
        fun `creates game`() {
            // Arrange
            val game = Game(
                id = 6L,
                homeTeamId = teamId1,
                awayTeamId = teamId2,
                homeGoals = 3,
                awayGoals = 2,
                homeTeam = team1,
                awayTeam = team2
            )
            every { gamesRepository.save(game) } returns game

            // Act
            val result = gamesService.create(game)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(game) },
            )
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `returns false when game not found`() {
            // Arrange
            val invalidGameId = 999L
            every { gamesRepository.findById(invalidGameId) } returns Optional.empty()

            // Act
            val result = gamesService.delete(invalidGameId)

            // Assert
            assertThat(result).isFalse()
        }

        @Test
        fun `deletes game`() {
            // Arrange
            val gameId = 1L
            val game = Game(id = gameId, homeTeamId = 1, awayTeamId = 2, homeGoals = 3, awayGoals = 2)
            every { gamesRepository.findById(gameId) } returns Optional.of(game)
            every { gamesRepository.delete(game) } returns Unit

            // Act
            val result = gamesService.delete(gameId)

            // Assert
            assertThat(result).isTrue()
        }
    }
}
