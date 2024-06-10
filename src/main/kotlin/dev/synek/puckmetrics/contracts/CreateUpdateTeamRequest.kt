package dev.synek.puckmetrics.contracts

import jakarta.validation.constraints.NotEmpty

data class CreateUpdateTeamRequest(
    @field:NotEmpty
    val location: String,

    @field:NotEmpty
    val name: String,

    @field:NotEmpty
    val abbreviation: String,

    val franchiseId: Long?,
)
