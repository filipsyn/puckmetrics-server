package dev.synek.puckmetrics.features.players

import dev.synek.puckmetrics.contracts.CreateUpdatePlayerRequest
import dev.synek.puckmetrics.contracts.PlayerDetailsResponse
import dev.synek.puckmetrics.contracts.PlayerInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(PlayersEndpointURLs.CONTROLLER_ROOT)
class PlayersController(
    private val playersService: PlayersService
) {
    @Operation(
        summary = "List all players",
        description = "Lists all players in the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Players were successfully retrieved.")
        ]
    )
    @GetMapping(PlayersEndpointURLs.GET_ALL_PLAYERS, produces = [APPLICATION_JSON])
    fun getAllPlayers(
        pageable: Pageable,
    ): ResponseEntity<List<PlayerInfoResponse>> {
        val players = playersService.get(pageable = pageable)
            .toList()
            .map(Player::toInfoResponse)

        return ResponseEntity.ok(players)
    }

    @Operation(
        summary = "Get player by ID",
        description = "Retrieves a player by its ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Player was successfully retrieved."),
            ApiResponse(responseCode = "404", description = "Player with the given ID was not found.")
        ]
    )
    @GetMapping(PlayersEndpointURLs.GET_PLAYER_BY_ID, produces = [APPLICATION_JSON])
    fun getPlayerById(
        @PathVariable id: Long,
    ): ResponseEntity<PlayerDetailsResponse> {
        val player = playersService.get(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(player.toDetailsResponse())
    }

    @Operation(
        summary = "Create a player",
        description = "Creates a new player."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Player was successfully created."),
            ApiResponse(responseCode = "400", description = "Invalid request body.")
        ]
    )
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

    @Operation(
        summary = "Update a player",
        description = "Updates a player."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Player was successfully updated."),
            ApiResponse(responseCode = "400", description = "Invalid request body."),
            ApiResponse(responseCode = "404", description = "Player with the given ID was not found.")
        ]
    )
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

    @Operation(
        summary = "Delete a player",
        description = "Deletes a player."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Player was successfully deleted."),
            ApiResponse(responseCode = "404", description = "Player with the given ID was not found.")
        ]
    )
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

