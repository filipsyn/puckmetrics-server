package dev.synek.puckmetrics.features.games

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class GamesService(
    @Autowired private val gamesRepository: GamesRepository
) {
    fun get(pageable: Pageable): List<Game> =
        gamesRepository.findAll(pageable).toList()

    fun get(id: Long): Game? = gamesRepository.findById(id).getOrNull()
}
