package dev.synek.puckmetrics.features.players

class PlayersService(
    private val playersRepository: PlayersRepository
) {
    fun get(): List<Player> {
        return playersRepository.findAll().toList()
    }
}
