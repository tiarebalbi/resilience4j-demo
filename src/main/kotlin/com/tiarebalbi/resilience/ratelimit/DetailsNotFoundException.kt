package com.tiarebalbi.resilience.ratelimit

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class DetailsNotFoundException(message: String) : Exception(message)
