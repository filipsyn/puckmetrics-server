package dev.synek.puckmetrics

import dev.synek.puckmetrics.common.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
class PuckmetricsApplicationTests : BaseIntegrationTest() {

    @Test
    fun contextLoads() {
    }

}
