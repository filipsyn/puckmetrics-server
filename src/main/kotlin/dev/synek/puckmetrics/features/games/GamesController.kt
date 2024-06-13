package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.contracts.CreateGameRequest
import dev.synek.puckmetrics.contracts.GameDetailsResponse
import dev.synek.puckmetrics.contracts.GameInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
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
    @GetMapping(GamesEndpointURLs.GET_ALL_GAMES)
    fun getAllGames(
        pageable: Pageable
    ): ResponseEntity<List<GameInfoResponse>> {
        val games = gamesService.get(pageable)
            .toList()
            .map(Game::toInfoResponse)

        return ResponseEntity.ok(games)
    }

    @GetMapping(GamesEndpointURLs.GET_GAME_BY_ID)
    fun getGameById(
        @PathVariable id: Long
    ): ResponseEntity<GameDetailsResponse> {
        val game = gamesService.get(id)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(game.toDetailsResponse())
    }

    @PostMapping(
        GamesEndpointURLs.CREATE_GAME,
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Valid
    fun createGame(
        @Valid @RequestBody request: CreateGameRequest
    ): ResponseEntity<GameDetailsResponse> {
        val game = gamesService.create(request.toEntity())

        return ResponseEntity
            .created(URI.create("/games/${game.id}"))
            .body(game.toDetailsResponse())
    }

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
