package dev.synek.puckmetrics.features.games.stats.skaters

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@Entity
@IdClass(GameSkaterStatsId::class)
data class GameSkaterStats(
    @Id
    @Column(name = "game_id")
    val gameId: Long? = null,

    @Id
    @Column(name = "player_id")
    val playerId: Long? = null,

    @Id
    @Column(name = "team_id")
    val teamId: Long? = null,

    @Column(name = "time_on_ice")
    val timeOnIce: Int = 0,

    @Column(name = "assists")
    val assists: Int = 0,

    @Column(name = "goals")
    val goals: Int = 0,

    @Column(name = "shots")
    val shots: Int = 0,

    @Column(name = "hits")
    val hits: Int = 0,

    @Column(name = "power_play_goals")
    val powerPlayGoals: Int = 0,

    @Column(name = "power_play_assists")
    val powerPlayAssists: Int = 0,

    @Column(name = "penalty_minutes")
    val penaltyMinutes: Int = 0,

    @Column(name = "face_off_wins")
    val faceOffWins: Int = 0,

    @Column(name = "face_offs_taken")
    val faceOffsTaken: Int = 0,

    @Column(name = "takeaways")
    val takeaways: Int = 0,

    @Column(name = "giveaways")
    val giveaways: Int = 0,

    @Column(name = "short_handed_goals")
    val shortHandedGoals: Int = 0,

    @Column(name = "short_handed_assists")
    val shortHandedAssists: Int = 0,

    @Column(name = "blocked")
    val blocked: Int = 0,

    @Column(name = "plus_minus")
    val plusMinus: Int = 0,

    @Column(name = "even_time_on_ice")
    val evenTimeOnIce: Int = 0,

    @Column(name = "short_handed_time_on_ice")
    val shortHandedTimeOnIce: Int = 0,

    @Column(name = "power_play_time_on_ice")
    val powerPlayTimeOnIce: Int = 0,
)

data class GameSkaterStatsId(
    @Column(name = "game_id")
    val gameId: Long,

    @Column(name = "player_id")
    val playerId: Long,

    @Column(name = "team_id")
    val teamId: Long,
) : Serializable
