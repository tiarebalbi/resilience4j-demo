package com.tiarebalbi.resilience.circuitbreaker

import com.tiarebalbi.resilience.common.API
import com.tiarebalbi.resilience.common.DemoCase
import com.tiarebalbi.resilience.common.RequestException
import com.tiarebalbi.resilience.common.Response
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.vavr.control.Try
import kotlin.system.measureTimeMillis

class CircuitBreakerDemo : DemoCase {

    override suspend fun run() {
        println("---- Running CircuitBreakerDemo ----")

        // Config
        val defaultConfig = CircuitBreakerConfig.ofDefaults()

        val overwrittenConfig = CircuitBreakerConfig
            .from(defaultConfig)
            .failureRateThreshold(40F)
            .minimumNumberOfCalls(1)
            .build()

        // Default Registry
        val circuitBreakerRegistry = CircuitBreakerRegistry.of(overwrittenConfig)

        // Init
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker("demo")

        // Printing current
        circuitBreaker.eventPublisher
            .onEvent { println("[State: ${circuitBreaker.state}] - FailureRateThreshold: ${circuitBreaker.metrics.failureRate}") }

        // Executing
        loopUsing(circuitBreaker)
    }

    /**
     * Executes a sequence of requests, each failure will trigger the recovery plan
     */
    private fun loopUsing(circuitBreaker: CircuitBreaker, numberOfTimes: Int = 100) {
        try {
            for (i in 1..numberOfTimes) {
                var result = ""
                val time = measureTimeMillis {
                    val decoratedSupplier = circuitBreaker.decorateSupplier { API.get("http://mock/users") }
                    result = Try.ofSupplier(decoratedSupplier)
                        .recover { throwable: Throwable? -> recovery(throwable) } // trying to recover the request
                        .get()
                }

                println("Time: $time - $result \n\n")
            }
        } catch (e: Exception) {
            println("Got to a limit:")
            println("NumberOfFailedCalls: ${circuitBreaker.metrics.numberOfFailedCalls}")
            println("NumberOfSuccessfulCalls: ${circuitBreaker.metrics.numberOfSuccessfulCalls}")
        }
    }

    /**
     * Example of a Recovery Plan
     */
    private fun recovery(it: Throwable?): Response? {
        if (it is RequestException) {
            println("Retrying due to: ${it.message}")
            Thread.sleep(1000) // Sleep another second
            return API.get("${it.url}/retry")
        }

        return API.get("${it?.message}/retry")
    }
}
