package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.*
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(StatsEndpointURLs.CONTROLLER_ROOT)
class StatsController(
    @Autowired private val statsService: StatsService,
) {
    @Operation(
        summary = "List best coaches",
        description = "List best coach in each season measured by number of games won."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Best coaches were successfully retrieved.")
        ]
    )
    @GetMapping(
        StatsEndpointURLs.GET_TOP_COACHES,
        produces = [APPLICATION_JSON]
    )
    fun getBestCoaches(): List<BestCoachInSeasonResponse> =
        statsService.getBestCoaches()

    @Operation(
        summary = "List top goal scorers",
        description = "List top goal scorers in each season measured by number of goals scored."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Top goal scorers were successfully retrieved.")
        ]
    )
    @GetMapping(
        StatsEndpointURLs.GET_TOP_GOAL_SCORERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopGoalScorers(): List<PlayerSeasonGoalsResponse> =
        statsService.getTopGoalScorers()


    @Operation(
        summary = "List top assisters",
        description = "List top assisters in each season measured by number of assists."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Top assisters were successfully retrieved.")
        ]
    )
    @GetMapping(
        StatsEndpointURLs.GET_TOP_ASSISTERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopAssisters(): List<PlayerSeasonAssistsResponse> =
        statsService.getTopAssisters()

    @Operation(
        summary = "List top point scorers",
        description = "List top point scorers in each season measured by number of points (goals + assists)."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Top point scorers were successfully retrieved.")
        ]
    )
    @GetMapping(
        StatsEndpointURLs.GET_TOP_POINT_SCORERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopPoints(): List<PlayerSeasonPointsResponse> =
        statsService.getTopPointScorers()

    @Operation(
        summary = "List top face-off takers",
        description = "List top face-off takers in each season measured by percentage of face-offs won."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Top face-off takers were successfully retrieved.")
        ]
    )
    @GetMapping(
        StatsEndpointURLs.GET_TOP_FACE_OFF_TAKERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopFaceOffTakers(): List<PlayerSeasonFaceOffResponse> =
        statsService.getTopFaceOffTakers()
}
