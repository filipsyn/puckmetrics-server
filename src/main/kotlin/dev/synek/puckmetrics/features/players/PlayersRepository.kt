package dev.synek.puckmetrics.features.players

import org.springframework.data.jpa.repository.JpaRepository

interface PlayersRepository : JpaRepository<Player, Long>
