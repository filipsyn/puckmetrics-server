package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.contracts.CreateUpdatePlayerRequest
import dev.synek.puckmetrics.contracts.PlayerDetailsResponse
import dev.synek.puckmetrics.contracts.PlayerInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PlayersEndpointURLs.CONTROLLER_ROOT)
class PlayersController(
    private val playersService: PlayersService
) {
    @GetMapping(PlayersEndpointURLs.GET_ALL_PLAYERS, produces = [APPLICATION_JSON])
    fun getAllPlayers(
        pageable: Pageable,
    ): ResponseEntity<List<PlayerInfoResponse>> {
        val players = playersService.get(pageable = pageable)
            .toList()
            .map(Player::toInfoResponse)

        return ResponseEntity.ok(players)
    }

    @GetMapping(PlayersEndpointURLs.GET_PLAYER_BY_ID, produces = [APPLICATION_JSON])
    fun getPlayerById(
        @PathVariable id: Long,
    ): ResponseEntity<PlayerDetailsResponse> {
        val player = playersService.get(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(player.toDetailsResponse())
    }

    @PostMapping(
        PlayersEndpointURLs.CREATE_PLAYER,
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    fun createPlayer(
        @RequestBody @Valid request: CreateUpdatePlayerRequest,
    ): ResponseEntity<PlayerDetailsResponse> {
        val player = playersService.create(request.toEntity())

        return ResponseEntity.ok(player.toDetailsResponse())
    }

    @PutMapping(
        PlayersEndpointURLs.UPDATE_PLAYER,
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    fun updatePlayer(
        @PathVariable id: Long,
        @RequestBody @Valid request: CreateUpdatePlayerRequest,
    ): ResponseEntity<PlayerDetailsResponse> {
        val player = request.toEntity()

        val updatedPlayer = playersService.update(id, player)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.accepted().body(updatedPlayer.toDetailsResponse())
    }

    @DeleteMapping(PlayersEndpointURLs.DELETE_PLAYER)
    fun deletePlayer(
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        val isDeleted = playersService.delete(id)

        if (!isDeleted) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.noContent().build()
    }
}

