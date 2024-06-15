package dev.synek.puckmetrics.contracts

data class BestCoachInSeasonResponse(
    val season: String,
    val coach: String,
    val wins: Int,
)
