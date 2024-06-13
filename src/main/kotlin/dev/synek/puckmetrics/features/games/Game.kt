package dev.synek.puckmetrics.features.games

import jakarta.persistence.*
import jakarta.validation.constraints.PositiveOrZero
import java.util.*

@Entity
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "season")
    val season: String? = null,

    @Column(name = "type")
    val type: String? = null,

    @Column(name = "date_time_utc")
    val dateTimeUtc: Date? = null,

    @Column(name = "away_team_id")
    val awayTeamId: Long? = null,

    @Column(name = "home_team_id")
    val homeTeamId: Long? = null,

    @PositiveOrZero
    @Column(name = "away_goals")
    val awayGoals: Int = 0,

    @PositiveOrZero
    @Column(name = "home_goals")
    val homeGoals: Int = 0,

    @Column(name = "outcome")
    val outcome: String? = null,

    @Column(name = "venue")
    val venueName: String? = null,
)
