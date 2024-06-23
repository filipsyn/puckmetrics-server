package dev.synek.puckmetrics.features.teams

import dev.synek.puckmetrics.contracts.CreateUpdateTeamRequest
import dev.synek.puckmetrics.contracts.TeamInfoResponse
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping(TeamsEndpointURLs.CONTROLLER_ROOT)
class TeamsController(
    private val teamsService: TeamsService,
) {
    @Operation(summary = "List all teams", description = "Lists all teams in the system.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Teams were successfully retrieved.")
        ]
    )
    @GetMapping(TeamsEndpointURLs.GET_ALL_TEAMS, produces = [APPLICATION_JSON])
    fun getAllTeams(
        pageable: Pageable
    ): ResponseEntity<List<TeamInfoResponse>> {
        val teams = teamsService.get(pageable)
            .toList()
            .map(Team::toResponse)

        return ResponseEntity.ok(teams)
    }

    @Operation(summary = "Get team by ID", description = "Retrieves a team by its ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Team was successfully retrieved."),
            ApiResponse(responseCode = "404", description = "Team with the given ID was not found.")
        ]
    )
    @GetMapping(TeamsEndpointURLs.GET_TEAM_BY_ID, produces = [APPLICATION_JSON])
    fun getTeamById(@PathVariable id: Long): ResponseEntity<TeamInfoResponse> {
        val team = teamsService.get(id)?.toResponse()
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(team)
    }

    @Operation(summary = "Create a team", description = "Creates a new team.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Team was successfully created."),
            ApiResponse(responseCode = "400", description = "Invalid request body.")
        ]
    )
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

    @Operation(summary = "Update a team", description = "Updates a team.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "202", description = "Team was successfully updated."),
            ApiResponse(responseCode = "400", description = "Invalid request body."),
            ApiResponse(responseCode = "404", description = "Team with the given ID was not found.")
        ]
    )
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

    @Operation(summary = "Delete a team", description = "Deletes a team.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Team was successfully deleted."),
            ApiResponse(responseCode = "404", description = "Team with the given ID was not found.")
        ]
    )
    @DeleteMapping(
        TeamsEndpointURLs.DELETE_TEAM,
        produces = [APPLICATION_JSON],
    )
    fun deleteTeam(@PathVariable id: Long): ResponseEntity<Unit> {
        val isDeleted = teamsService.delete(id)

        if (!isDeleted) {
            return ResponseEntity.notFound().build()
        }

        return ResponseEntity.noContent().build()
    }
}
