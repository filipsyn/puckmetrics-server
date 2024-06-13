package dev.synek.puckmetrics.features.games.stats.teams

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import java.io.Serializable

@Entity
@IdClass(GameTeamStatsId::class)
data class GameTeamStats(
    @Id
    @Column(name = "game_id")
    val gameId: Long? = null,

    @Id
    @Column(name = "team_id")
    val teamId: Long? = null,

    @Column(name = "home_or_away")
    val homeOrAway: String? = null,

    @Column(name = "won")
    val won: Boolean = false,

    @Column(name = "settled_in")
    val settledIn: String? = null,

    @Column(name = "head_coach")
    val headCoach: String? = null,

    @Column(name = "goals")
    val goals: Int = 0,

    @Column(name = "shots")
    val shots: Int = 0,

    @Column(name = "hits")
    val hits: Int = 0,

    @Column(name = "penalty_minutes")
    val penaltyMinutes: Int = 0,

    @Column(name = "power_play_opportunities")
    val powerPlayOpportunities: Int = 0,

    @Column(name = "power_play_goals")
    val powerPlayGoals: Int = 0,

    @Column(name = "face_off_win_percentage")
    val faceOffWinPercentage: Double = 0.0,

    @Column(name = "giveaways")
    val giveaways: Int = 0,

    @Column(name = "takeaways")
    val takeaways: Int = 0,

    @Column(name = "blocked")
    val blocked: Int = 0,

    @Column(name = "start_rink_side")
    val startRingSide: String? = null,
)

data class GameTeamStatsId(
    @Column(name = "game_id")
    val gameId: Long,

    @Column(name = "team_id")
    val teamId: Long,
) : Serializable


