package dev.synek.puckmetrics.common

import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
abstract class BaseIntegrationTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            SingletonPostgresContainer.instance
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            val container = SingletonPostgresContainer.instance
            registry.add("spring.datasource.url") { container.jdbcUrl }
            registry.add("spring.datasource.username") { container.username }
            registry.add("spring.datasource.password") { container.password }
        }
    }
}
