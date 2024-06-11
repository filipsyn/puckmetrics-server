package dev.synek.puckmetrics.features.players

import org.springframework.data.repository.CrudRepository

interface PlayersRepository : CrudRepository<Player, Long>
