package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.features.games.Game
import dev.synek.puckmetrics.features.games.stats.skaters.GameSkaterStats
import jakarta.persistence.*


@Entity
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "franchise_id")
    val franchiseId: Long? = null,

    @Column(name = "location")
    val location: String? = null,

    @Column(name = "name")
    val name: String? = null,

    @Column(name = "abbreviation")
    val abbreviation: String = "",

    @OneToMany(mappedBy = "homeTeam")
    val homeGames: Collection<Game> = emptyList(),

    @OneToMany(mappedBy = "awayTeam")
    val awayGames: Collection<Game> = emptyList(),

    @OneToMany(mappedBy = "team")
    val gameSkaterStats: Collection<GameSkaterStats> = emptyList(),
)
