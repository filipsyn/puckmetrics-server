package dev.synek.puckmetrics.features.players

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.util.*

class PlayersServiceUnitTest {
    private val playersRepository: PlayersRepository = mockk<PlayersRepository>()
    private val playersService: PlayersService = PlayersService(playersRepository)

    val players = listOf(
        Player(firstName = "Jaromir", lastName = "Jagr"),
        Player(firstName = "Wayne", lastName = "Gretzky"),
        Player(firstName = "Mario", lastName = "Lemieux"),
        Player(firstName = "Bobby", lastName = "Orr"),
        Player(firstName = "Gordie", lastName = "Howe"),
        Player(firstName = "Maurice", lastName = "Richard"),
    )

    @Nested
    inner class GetAll {
        @Test
        fun `returns empty list when no players are present`() {
            // Arrange
            every { playersRepository.findAll() } returns emptyList()

            // Act
            val result = playersService.get()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result.count()).isEqualTo(0) },
                { assertThat(result).isEqualTo(emptyList<Player>()) },
            )
        }

        @Test
        fun `returns all players when present`() {
            // Arrange
            every { playersRepository.findAll() } returns players

            // Act
            val result = playersService.get()

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(players) },
            )
        }
    }

    @Nested
    inner class GetById {
        @Test
        fun `returns null when player is not present`() {
            // Arrange
            val invalidPlayerId = 999L
            every { playersRepository.findById(invalidPlayerId) } returns Optional.empty()

            // Act
            val result = playersService.get(invalidPlayerId)

            // Assert
            assertThat(result).isNull()
        }

        @Test
        fun `returns player when present`() {
            // Arrange
            val playerId = 1L
            val player = Player(id = playerId, firstName = "Jaromir", lastName = "Jagr")

            every { playersRepository.findById(playerId) } returns Optional.of(player)

            // Act
            val result = playersService.get(playerId)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(player) },
            )
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `creates player`() {
            // Arrange
            val player = Player(firstName = "Jaromir", lastName = "Jagr")
            every { playersRepository.save(player) } returns player

            // Act
            val result = playersService.create(player)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(player) },
                { verify { playersRepository.save(player) } }
            )
        }
    }
}
