package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.contracts.PlayerInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(PlayersEndpointURLs.CONTROLLER_ROOT)
class PlayersController(
    private val playersService: PlayersService
) {
    @GetMapping(PlayersEndpointURLs.GET_ALL_PLAYERS, produces = [APPLICATION_JSON])
    fun getAllPlayers(): ResponseEntity<List<PlayerInfoResponse>> {
        val players = playersService.get()
            .toList()
            .map(Player::toResponse)

        return ResponseEntity.ok(players)
    }

    @GetMapping(PlayersEndpointURLs.GET_PLAYER_BY_ID, produces = [APPLICATION_JSON])
    fun getPlayerById(
        @PathVariable id: Long,
    ): ResponseEntity<PlayerInfoResponse> {
        val player = playersService.get(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(player.toResponse())
    }
}

