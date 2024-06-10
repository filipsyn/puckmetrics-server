package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.contracts.CreateUpdateTeamRequest
import dev.synek.puckmetrics.contracts.TeamInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping(TeamsEndpointURLs.CONTROLLER_ROOT)
class TeamsController(
    private val teamsService: TeamsService,
) {
    @GetMapping(TeamsEndpointURLs.GET_ALL_TEAMS, produces = [APPLICATION_JSON])
    fun getAllTeams(): ResponseEntity<List<TeamInfoResponse>> {
        val teams = teamsService.get()
            .toList()
            .map(Team::toResponse)

        return ResponseEntity.ok(teams)
    }

    @GetMapping(TeamsEndpointURLs.GET_TEAM_BY_ID, produces = [APPLICATION_JSON])
    fun getTeamById(@PathVariable id: Long): ResponseEntity<TeamInfoResponse> {
        val team = teamsService.get(id)?.toResponse()
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(team)
    }

    @PostMapping(
        TeamsEndpointURLs.CREATE_TEAM,
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Valid
    fun createTeam(
        @RequestBody @Valid request: CreateUpdateTeamRequest
    ): ResponseEntity<TeamInfoResponse> {
        val team = request.toEntity()

        val createdTeam = teamsService.create(team)

        return ResponseEntity.created(URI.create("/teams/${createdTeam.id}")).body(createdTeam.toResponse())
    }

    @PutMapping(
        TeamsEndpointURLs.UPDATE_TEAM,
        consumes = [APPLICATION_JSON],
        produces = [APPLICATION_JSON]
    )
    @Valid
    fun updateTeam(
        @PathVariable id: Long,
        @RequestBody @Valid request: CreateUpdateTeamRequest
    ): ResponseEntity<TeamInfoResponse> {
        val team = request.toEntity()

        val updatedTeam = teamsService.update(id, team)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity
            .accepted()
            .body(updatedTeam.toResponse())
    }

    @DeleteMapping(TeamsEndpointURLs.DELETE_TEAM)
    fun deleteTeam(@PathVariable id: Long): ResponseEntity<Unit> {
        val isDeleted = teamsService.delete(id)

        if (!isDeleted) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.noContent().build()
    }
}
