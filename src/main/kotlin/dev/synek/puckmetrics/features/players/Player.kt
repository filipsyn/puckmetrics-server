package dev.synek.puckmetrics.features.players

import jakarta.persistence.*
import java.util.*

@Entity
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "first_name")
    val firstName: String? = null,

    @Column(name = "last_name")
    val lastName: String? = null,

    @Column(name = "nationality")
    val nationality: String? = null,

    @Column(name = "birth_city")
    val birthCity: String? = null,

    @Column(name = "position")
    val position: String? = null,

    @Column(name = "birth_date")
    val birthDate: Date? = null,

    @Column(name = "height")
    val height: Int? = null,

    @Column(name = "weight")
    val weight: Int? = null,

    @Column(name = "shoots")
    val shoots: String? = null,
)
