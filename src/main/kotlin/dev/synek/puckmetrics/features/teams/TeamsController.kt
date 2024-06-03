package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(TeamsEndpointURLs.CONTROLLER_ROOT)
class TeamsController(
    private val teamsService: TeamsService,
) {
    @GetMapping(TeamsEndpointURLs.GET_ALL_TEAMS, produces = [APPLICATION_JSON])
    fun getAllTeams(): List<Team> = teamsService.get().toList()
}
