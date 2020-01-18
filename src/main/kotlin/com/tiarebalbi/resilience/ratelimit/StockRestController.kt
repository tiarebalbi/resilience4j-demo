package com.tiarebalbi.resilience.ratelimit

import com.tiarebalbi.resilience.ratelimit.model.ErrorDetails
import com.tiarebalbi.resilience.ratelimit.model.Stock
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/stock")
@RateLimiter(name = "stockAPI")
class StockRestController(private val stockData: List<Stock>) {

    @GetMapping
    fun getAllStocks() = stockData

    @GetMapping("/{symbol}")
    fun searchStock(@PathVariable symbol: String): Stock =
        stockData.findBySymbol(symbol)

    @ExceptionHandler(RequestNotPermitted::class)
    fun exceptionHandler(exception: RequestNotPermitted): ResponseEntity<ErrorDetails> =
        asResponseEntityWith("You reach the maximum number of API calls")
}

fun List<Stock>.findBySymbol(symbol: String): Stock {
    return this.firstOrNull { it.symbol.toLowerCase() == symbol.toLowerCase() }
        ?: throw DetailsNotFoundException("Stock $symbol not found")
}

fun asResponseEntityWith(message: String): ResponseEntity<ErrorDetails> {
    return ResponseEntity(
        ErrorDetails(
            message,
            LocalDateTime.now()
        ), HttpStatus.TOO_MANY_REQUESTS
    )
}

