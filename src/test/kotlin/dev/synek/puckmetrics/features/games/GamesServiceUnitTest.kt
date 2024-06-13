package dev.synek.puckmetrics.features.games

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.*

class GamesServiceUnitTest {
    private val gamesRepository = mockk<GamesRepository>()
    private val gamesService = GamesService(gamesRepository)
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
    inner class Create {
        @Test
        fun `creates game`() {
            // Arrange
            val game = Game(id = 6L, homeTeamId = 1, awayTeamId = 2, homeGoals = 3, awayGoals = 2)
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
}
