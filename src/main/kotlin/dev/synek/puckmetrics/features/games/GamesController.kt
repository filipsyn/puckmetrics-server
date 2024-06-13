package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.contracts.GameInfoResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
