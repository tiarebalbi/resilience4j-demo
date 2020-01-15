package com.tiarebalbi.resilience.ratelimit.model

import java.time.LocalDateTime

data class ErrorDetails(val message: String, val timestamp: LocalDateTime)
