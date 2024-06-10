package dev.synek.puckmetrics.features.players

class PlayersService(
    private val playersRepository: PlayersRepository
) {
    fun get(): List<Player> =
        playersRepository.findAll().toList()

}
