package dev.synek.puckmetrics.features.games.stats.goalies

import dev.synek.puckmetrics.features.games.Game
import dev.synek.puckmetrics.features.players.Player
import dev.synek.puckmetrics.features.teams.Team
import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(GameGoalieStatsId::class)
data class GameGoalieStats(
    @Id
    @Column(name = "game_id")
    val gameId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "game_id", insertable = false, updatable = false)
    val game: Game? = null,

    @Id
    @Column(name = "player_id")
    val playerId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "player_id", insertable = false, updatable = false)
    val player: Player? = null,

    @Id
    @Column(name = "team_id")
    val teamId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    val team: Team? = null,

    @Column(name = "time_on_ice")
    val timeOnIce: Int = 0,

    @Column(name = "assists")
    val assists: Int = 0,

    @Column(name = "goals")
    val goals: Int = 0,

    @Column(name = "penalty_minutes")
    val penaltyMinutes: Int = 0,

    @Column(name = "shots")
    val shots: Int = 0,

    @Column(name = "saves")
    val saves: Int = 0,

    @Column(name = "power_play_saves")
    val powerPlaySaves: Int = 0,

    @Column(name = "short_handed_saves")
    val shortHandedSaves: Int = 0,

    @Column(name = "even_saves")
    val evenSaves: Int = 0,

    @Column(name = "short_handed_shots_against")
    val shortHandedShotsAgainst: Int = 0,

    @Column(name = "even_shots_against")
    val evenShotsAgainst: Int = 0,

    @Column(name = "power_play_shots_against")
    val powerPlayShotsAgainst: Int = 0,

    @Column(name = "decision")
    val decision: String? = null,

    @Column(name = "save_percentage")
    val savePercentage: Double = 0.0,

    @Column(name = "power_play_save_percentage")
    val powerPlaySavePercentage: Double = 0.0,

    @Column(name = "even_strength_save_percentage")
    val evenStrengthSavePercentage: Double = 0.0,
)

data class GameGoalieStatsId(
    @Id
    @Column(name = "game_id")
    val gameId: Long? = null,

    @Id
    @Column(name = "player_id")
    val playerId: Long? = null,

    @Id
    @Column(name = "team_id")
    val teamId: Long? = null,
) : Serializable
