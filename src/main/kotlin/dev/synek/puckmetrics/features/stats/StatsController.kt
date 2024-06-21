package dev.synek.puckmetrics.features.stats

import dev.synek.puckmetrics.contracts.BestCoachInSeasonResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonAssistsResponse
import dev.synek.puckmetrics.contracts.PlayerSeasonGoalsResponse
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
        StatsEndpointURLs.BEST_COACHES,
        produces = [APPLICATION_JSON]
    )
    fun getBestCoaches(): List<BestCoachInSeasonResponse> =
        statsService.getBestCoaches()


    @GetMapping(
        StatsEndpointURLs.TOP_GOAL_SCORERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopGoalScorers(): List<PlayerSeasonGoalsResponse> =
        statsService.getTopGoalScorers()


    @GetMapping(
        StatsEndpointURLs.TOP_ASSISTERS,
        produces = [APPLICATION_JSON]
    )
    fun getTopAssisters(): List<PlayerSeasonAssistsResponse> =
        statsService.getTopAssisters()
}
