package dev.synek.puckmetrics.features.teams

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Tag("integration")
class TeamsRepositoryIntegrationTest(
    @Autowired private val teamsRepository: TeamsRepository
) {
    companion object {
        @Container
        val postgresContainer = PostgreSQLContainer<Nothing>(
            DockerImageName.parse("postgres:16")
        ).apply {
            withDatabaseName("integration-tests-db")
            withUsername("test-user")
            withPassword("test-password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun datasourceConfig(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
        }
    }

    private val teams = listOf(
        Team(id = 1, location = "New York", name = "Rangers"),
        Team(id = 2, location = "Montreal", name = "Canadiens"),
        Team(id = 3, location = "Chicago", name = "Blackhawks"),
        Team(id = 4, location = "Detroit", name = "Red Wings"),
        Team(id = 5, location = "Boston", name = "Bruins"),
        Team(id = 6, location = "Toronto", name = "Maple Leafs"),
    )

    @Nested
    inner class FindAll {
        init {
            teamsRepository.saveAll(teams)
        }

        @Test
        fun `returns all teams`() {
            // Act
            val result = teamsRepository.findAll()

            // Assert
            assertThat(result.count()).isEqualTo(teams.count())
            assertThat(result).isEqualTo(teams)
        }
    }
}
