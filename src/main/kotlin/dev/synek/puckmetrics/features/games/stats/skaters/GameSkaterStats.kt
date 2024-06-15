package dev.synek.puckmetrics.features.games.stats.skaters

import dev.synek.puckmetrics.features.games.Game
import dev.synek.puckmetrics.features.players.Player
import dev.synek.puckmetrics.features.teams.Team
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.io.Serializable

@Entity
@IdClass(GameSkaterStatsId::class)
data class GameSkaterStats(
    @Id
    @Column(name = "game_id")
    val gameId: Long? = null,

    @ManyToOne
    @NotNull
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    val game: Game? = null,

    @Id
    @Column(name = "player_id")
    val playerId: Long? = null,

    @ManyToOne
    @NotNull
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    val player: Player? = null,

    @Id
    @Column(name = "team_id")
    val teamId: Long? = null,

    @ManyToOne
    @NotNull
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    val team: Team? = null,

    @Column(name = "time_on_ice")
    val timeOnIce: Int? = 0,

    @Column(name = "assists")
    val assists: Int? = 0,

    @Column(name = "goals")
    val goals: Int? = 0,

    @Column(name = "shots")
    val shots: Int? = 0,

    @Column(name = "hits")
    val hits: Int? = null,

    @Column(name = "power_play_goals")
    val powerPlayGoals: Int? = 0,

    @Column(name = "power_play_assists")
    val powerPlayAssists: Int? = 0,

    @Column(name = "penalty_minutes")
    val penaltyMinutes: Int? = 0,

    @Column(name = "face_off_wins")
    val faceOffWins: Int? = 0,

    @Column(name = "face_offs_taken")
    val faceOffsTaken: Int? = 0,

    @Column(name = "takeaways")
    val takeaways: Int? = 0,

    @Column(name = "giveaways")
    val giveaways: Int? = 0,

    @Column(name = "short_handed_goals")
    val shortHandedGoals: Int? = 0,

    @Column(name = "short_handed_assists")
    val shortHandedAssists: Int? = 0,

    @Column(name = "blocked")
    val blocked: Int? = 0,

    @Column(name = "plus_minus")
    val plusMinus: Int? = 0,

    @Column(name = "even_time_on_ice")
    val evenTimeOnIce: Int = 0,

    @Column(name = "short_handed_time_on_ice")
    val shortHandedTimeOnIce: Int = 0,

    @Column(name = "power_play_time_on_ice")
    val powerPlayTimeOnIce: Int = 0,
)

data class GameSkaterStatsId(
    val gameId: Long? = null,

    val playerId: Long? = null,

    val teamId: Long? = null
) : Serializable
