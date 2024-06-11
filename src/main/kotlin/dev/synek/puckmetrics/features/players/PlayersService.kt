package dev.synek.puckmetrics.features.players

import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class PlayersService(
    private val playersRepository: PlayersRepository
) {
    fun get(): List<Player> =
        playersRepository.findAll().toList()

    fun get(id: Long): Player? =
        playersRepository.findById(id).getOrNull()

    fun create(player: Player): Player =
        playersRepository.save(player)

    fun update(id: Long, player: Player): Player? {
        val existingPlayer = playersRepository.findById(id).getOrNull()
            ?: return null

        val updatedPlayer = existingPlayer.copy(
            firstName = player.firstName,
            lastName = player.lastName,
            nationality = player.nationality,
            birthCity = player.birthCity,
            position = player.position,
            birthDate = player.birthDate,
            height = player.height,
            weight = player.weight,
            shoots = player.shoots
        )

        return playersRepository.save(updatedPlayer)
    }

    fun delete(id: Long): Boolean {
        val player = playersRepository.findById(id).getOrNull()
            ?: return false

        playersRepository.delete(player)
        return true
    }
}
