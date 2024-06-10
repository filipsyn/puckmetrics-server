package dev.synek.puckmetrics.features.players

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

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
}
