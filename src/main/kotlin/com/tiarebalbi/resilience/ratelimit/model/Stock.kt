package com.tiarebalbi.resilience.ratelimit.model

import java.time.LocalTime

data class Stock(
    val name: String,
    val symbol: String,
    val type: String,
    val region: String,
    val marketOpen: LocalTime,
    val marketClose: LocalTime,
    val timezone: String,
    val currency: String,
    val matchScore: Double
)