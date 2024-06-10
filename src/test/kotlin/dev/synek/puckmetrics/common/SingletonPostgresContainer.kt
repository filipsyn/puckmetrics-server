package dev.synek.puckmetrics.common

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object SingletonPostgresContainer {
    val instance: PostgreSQLContainer<*> by lazy {
        PostgreSQLContainer(
            DockerImageName.parse("postgres:latest")
        ).apply {
            withDatabaseName("integration-tests-db")
            withUsername("test-user")
            withPassword("test-password")
            start()
        }
    }
}
