package dev.synek.puckmetrics.features.players

import kotlin.jvm.optionals.getOrNull

class PlayersService(
    private val playersRepository: PlayersRepository
) {
    fun get(): List<Player> =
        playersRepository.findAll().toList()

    fun get(id: Long): Player? =
        playersRepository.findById(id).getOrNull()

}
