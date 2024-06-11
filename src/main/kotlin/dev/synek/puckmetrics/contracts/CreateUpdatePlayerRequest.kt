package dev.synek.puckmetrics.contracts

import dev.synek.puckmetrics.shared.Constants
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.Range
import java.util.*

data class CreateUpdatePlayerRequest(
    @field:NotEmpty
    val firstName: String,

    @field:NotEmpty
    val lastName: String,

    @field:NotEmpty
    val nationality: String,

    @field:NotEmpty
    val birthCity: String,

    @field:NotEmpty
    val position: String,

    @field:NotNull
    @field:Past
    val birthDate: Date,

    @field:Range(
        min = Constants.Player.MINIMUM_HEIGHT,
        max = Constants.Player.MAXIMUM_HEIGHT
    )
    val height: Int,

    @field:Range(
        min = Constants.Player.MINIMUM_WEIGHT,
        max = Constants.Player.MAXIMUM_WEIGHT
    )
    val weight: Int,

    @field:Size(min = 1, max = 1)
    val shoots: String,
)
