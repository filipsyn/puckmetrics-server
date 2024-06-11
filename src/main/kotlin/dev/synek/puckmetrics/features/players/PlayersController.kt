package dev.synek.puckmetrics.features.players

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PlayersEndpointURLs.CONTROLLER_ROOT)
class PlayersController(
    private val playersService: PlayersService
)

