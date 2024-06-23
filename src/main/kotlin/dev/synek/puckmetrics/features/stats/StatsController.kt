package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.*
import dev.synek.puckmetrics.shared.ControllerConstants.APPLICATION_JSON
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(StatsEndpointURLs.CONTROLLER_ROOT)
class StatsController(
    @Autowired private val statsService: StatsService,
) {
    @GetMapping(
        StatsEndpointURLs.GET_TOP_COACHES,
        produces = [APPLICATION_JSON]
    )
    fun getBestCoaches(): List<BestCoachInSeasonResponse> =
        statsService.getBestCoaches()


    @GetMapping(
        StatsEndpointURLs.GET_TOP_GOAL_SCORERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopGoalScorers(): List<PlayerSeasonGoalsResponse> =
        statsService.getTopGoalScorers()


    @GetMapping(
        StatsEndpointURLs.GET_TOP_ASSISTERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopAssisters(): List<PlayerSeasonAssistsResponse> =
        statsService.getTopAssisters()

    @GetMapping(
        StatsEndpointURLs.GET_TOP_POINT_SCORERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopPoints(): List<PlayerSeasonPointsResponse> =
        statsService.getTopPointScorers()

    @GetMapping(
        StatsEndpointURLs.GET_TOP_FACE_OFF_TAKERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopFaceOffTakers(): List<PlayerSeasonFaceOffResponse> =
        statsService.getTopFaceOffTakers()
}
