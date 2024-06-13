package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.features.teams.Team
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
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

    @ManyToOne
    @NotNull
    @JoinColumn(name = "home_team_id", insertable = false, updatable = false)
    val awayTeam: Team? = null,

    @Column(name = "away_team_id")
    val awayTeamId: Long? = null,

    @ManyToOne
    @NotNull
    @JoinColumn(name = "home_team_id", insertable = false, updatable = false)
    val homeTeam: Team? = null,

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
