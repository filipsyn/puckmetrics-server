package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.contracts.CreateUpdatePlayerRequest
import dev.synek.puckmetrics.contracts.PlayerDetailsResponse
import dev.synek.puckmetrics.contracts.PlayerInfoResponse

fun Player.toInfoResponse() = PlayerInfoResponse(
    id = id,
    firstName = firstName,
    lastName = lastName,
)

fun Player.toDetailsResponse() = PlayerDetailsResponse(
    id = id,
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
