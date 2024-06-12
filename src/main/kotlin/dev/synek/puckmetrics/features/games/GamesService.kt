package dev.synek.puckmetrics.features.games

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GamesService(
    @Autowired private val gamesRepository: GamesRepository
)
