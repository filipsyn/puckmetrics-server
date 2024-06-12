package dev.synek.puckmetrics.features.games

import org.springframework.data.jpa.repository.JpaRepository

interface GamesRepository : JpaRepository<Game, Long>
