package dev.synek.puckmetrics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PuckmetricsApplication

fun main(args: Array<String>) {
    runApplication<PuckmetricsApplication>(*args)
}
