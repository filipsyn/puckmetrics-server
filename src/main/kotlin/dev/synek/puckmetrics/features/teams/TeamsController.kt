package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(TeamsEndpointURLs.CONTROLLER_ROOT)
class TeamsController(
    private val teamsService: TeamsService,
) {
    @GetMapping(TeamsEndpointURLs.GET_ALL_TEAMS, produces = [APPLICATION_JSON])
    fun getAllTeams(): ResponseEntity<List<Team>> {
        val teams = teamsService.get().toList()

        return ResponseEntity.ok(teams)
    }

    @GetMapping(TeamsEndpointURLs.GET_TEAM_BY_ID, produces = [APPLICATION_JSON])
    fun getTeamById(@PathVariable id: Long): ResponseEntity<Team?> {
        val team = teamsService.get(id) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(team)
    }
}
