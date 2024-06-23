package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.contracts.CreateGameRequest
import dev.synek.puckmetrics.contracts.GameDetailsResponse
import dev.synek.puckmetrics.contracts.GameInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping(GamesEndpointURLs.CONTROLLER_ROOT)
class GamesController(
    @Autowired private val gamesService: GamesService,
) {
    @Operation(
        summary = "List all games",
        description = "Lists all games in the system."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Games were successfully retrieved.")
        ]
    )
    @GetMapping(GamesEndpointURLs.GET_ALL_GAMES)
    fun getAllGames(
        pageable: Pageable
    ): ResponseEntity<List<GameInfoResponse>> {
        val games = gamesService.get(pageable)
            .toList()
            .map(Game::toInfoResponse)

        return ResponseEntity.ok(games)
    }

    @Operation(
        summary = "Get game by ID",
        description = "Retrieves a game by its ID."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Game was successfully retrieved."),
            ApiResponse(responseCode = "404", description = "Game with the given ID was not found.")
        ]
    )
    @GetMapping(GamesEndpointURLs.GET_GAME_BY_ID)
    fun getGameById(
        @PathVariable id: Long
    ): ResponseEntity<GameDetailsResponse> {
        val game = gamesService.get(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(game.toDetailsResponse())
    }

    @Operation(
        summary = "Create a game",
        description = "Creates a new game."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Game was successfully created."),
            ApiResponse(responseCode = "400", description = "Invalid request body.")
        ]
    )
    @PostMapping(
        GamesEndpointURLs.CREATE_GAME,
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Valid
    fun createGame(
        @Valid @RequestBody request: CreateGameRequest
    ): ResponseEntity<GameDetailsResponse> {
        val game = try {
            gamesService.create(request.toEntity())
        } catch (e: IllegalArgumentException) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity
            .created(URI.create("/games/${game.id}"))
            .body(game.toDetailsResponse())
    }

    @Operation(
        summary = "Delete a game",
        description = "Deletes a game."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Game was successfully deleted."),
            ApiResponse(responseCode = "404", description = "Game with the given ID was not found.")
        ]
    )
    @DeleteMapping(GamesEndpointURLs.DELETE_GAME)
    fun deleteGame(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        val isDeleted = gamesService.delete(id)

        if (!isDeleted) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.noContent().build()
    }
}
