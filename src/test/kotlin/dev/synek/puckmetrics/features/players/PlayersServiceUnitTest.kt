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

    @Nested
    inner class Update {
        @Test
        fun `returns null when ID is invalid`() {
            // Arrange
            val invalidPlayerId = 999L
            val player = Player(id = invalidPlayerId, firstName = "Jaromir", lastName = "Jagr")
            every { playersRepository.findById(invalidPlayerId) } returns Optional.empty()

            // Act
            val result = playersService.update(invalidPlayerId, player)

            // Assert
            assertThat(result).isNull()
        }

        @Test
        fun `updates player and returns him`() {
            // Arrange
            val playerId = 1L
            val originalPlayer = Player(id = playerId, firstName = "Jaromir", lastName = "Jagr")
            every { playersRepository.findById(playerId) } returns Optional.of(originalPlayer)

            val updatedPlayer = originalPlayer.copy(firstName = "David", lastName = "Pastrnak")

            every { playersRepository.save(updatedPlayer) } returns updatedPlayer

            // Act
            val result = playersService.update(playerId, updatedPlayer)

            // Assert
            assertAll(
                { assertThat(result).isNotNull() },
                { assertThat(result).isEqualTo(updatedPlayer) },
                { verify { playersRepository.save(updatedPlayer) } }
            )
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `returns false when player is not present`() {
            // Arrange
            val invalidPlayerId = 999L
            every { playersRepository.findById(invalidPlayerId) } returns Optional.empty()
            every { playersRepository.delete(any()) }

            // Act
            val result = playersService.delete(invalidPlayerId)

            // Assert
            assertThat(result).isFalse()
        }

        @Test
        fun `deletes player and returns true`() {
            // Arrange
            val playerId = 1L
            val player = Player(id = playerId, firstName = "Jaromir", lastName = "Jagr")
            every { playersRepository.findById(playerId) } returns Optional.of(player)
            every { playersRepository.delete(any()) } returns Unit

            // Act
            val result = playersService.delete(playerId)

            // Assert
            assertAll(
                { assertThat(result).isTrue() },
                { verify { playersRepository.delete(player) } }
            )
        }
    }
}
