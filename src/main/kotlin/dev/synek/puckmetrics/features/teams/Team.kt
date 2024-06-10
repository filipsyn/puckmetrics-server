package dev.synek.puckmetrics.features.teams

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val franchiseId: Long? = null,

    val location: String? = null,

    val name: String? = null,

    val abbreviation: String = "",
)
