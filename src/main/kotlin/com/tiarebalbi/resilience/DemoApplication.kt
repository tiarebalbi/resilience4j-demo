package com.tiarebalbi.resilience

import com.tiarebalbi.resilience.circuitbreaker.CircuitBreakerDemo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    // Circuit Breaker Demo
    CircuitBreakerDemo().run()

    // Run Spring Boot app
    runApplication<DemoApplication>(*args)
}
