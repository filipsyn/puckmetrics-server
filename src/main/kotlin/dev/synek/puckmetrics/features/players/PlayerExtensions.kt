package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.contracts.PlayerInfoResponse

fun Player.toResponse() = PlayerInfoResponse(
    id = id,
    firstName = firstName,
    lastName = lastName
)
