package dev.synek.puckmetrics.contracts

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import java.util.*

data class CreateGameRequest(
    @field:Size(min = 8, max = 8)
    val season: String,

    @field:Size(min = 1, max = 1)
    val type: String,

    @field:NotNull
    val dateTimeUtc: Date,

    val awayTeamId: Long,

    val homeTeamId: Long,

    @field:PositiveOrZero
    val awayGoals: Int = 0,

    @field:PositiveOrZero
    val homeGoals: Int = 0,

    @field:NotEmpty
    val outcome: String,

    @field:NotEmpty
    val venueName: String,
)
