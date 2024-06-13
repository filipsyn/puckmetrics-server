package dev.synek.puckmetrics.features.games

import dev.synek.puckmetrics.features.teams.Team
import dev.synek.puckmetrics.features.teams.TeamsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class GamesService(
    @Autowired private val gamesRepository: GamesRepository,
    @Autowired private val teamsRepository: TeamsRepository,
) {
    fun get(pageable: Pageable): List<Game> =
        gamesRepository.findAll(pageable).toList()

    fun get(id: Long): Game? = gamesRepository.findById(id).getOrNull()

    fun create(game: Game): Game {
        val (homeTeam, awayTeam) = validateTeams(game)

        val connectedGame = game.copy(awayTeam = awayTeam, homeTeam = homeTeam)

        return gamesRepository.save(connectedGame)
    }

    fun delete(id: Long): Boolean {
        val game = gamesRepository.findById(id).getOrNull()
            ?: return false

        gamesRepository.delete(game)
        return true
    }

    private fun validateTeams(game: Game): Pair<Team, Team> {
        val homeTeam = game.homeTeamId?.let { teamsRepository.findById(it).getOrNull() }
            ?: throw IllegalArgumentException("Home team not found")

        val awayTeam = game.awayTeamId?.let { teamsRepository.findById(it).getOrNull() }
            ?: throw IllegalArgumentException("Away team not found")

        return Pair(homeTeam, awayTeam)
    }
}
