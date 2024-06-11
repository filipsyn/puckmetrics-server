package dev.synek.puckmetrics.contracts

import java.util.*

data class PlayerDetailsResponse(
    val id: Long?,
    val firstName: String?,
    val lastName: String?,
    val nationality: String?,
    val birthCity: String?,
    val position: String?,
    val birthDate: Date?,
    val height: Int?,
    val weight: Int?,
    val shoots: String?,
)
