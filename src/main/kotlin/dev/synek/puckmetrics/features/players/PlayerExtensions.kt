package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.contracts.CreateUpdatePlayerRequest
import dev.synek.puckmetrics.contracts.PlayerInfoResponse

fun Player.toResponse() = PlayerInfoResponse(
    id = id,
    firstName = firstName,
    lastName = lastName
)

fun CreateUpdatePlayerRequest.toEntity() = Player(
    firstName = firstName,
    lastName = lastName,
    nationality = nationality,
    birthCity = birthCity,
    position = position,
    birthDate = birthDate,
    height = height,
    weight = weight,
    shoots = shoots,
)
