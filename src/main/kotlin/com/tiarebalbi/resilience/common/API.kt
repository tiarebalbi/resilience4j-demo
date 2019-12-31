package com.tiarebalbi.resilience.common

import java.util.concurrent.atomic.AtomicInteger

typealias Response = String

class RequestException(val url: String, message: String) : RuntimeException(message)

class API {
    companion object {
        private val counter = AtomicInteger(1)

        fun get(url: String): Response {
            val fakeError = counter.getAndIncrement()
            println("$fakeError - $url")

            if (fakeError.rem(3) == 0) {
                throw RequestException(url, "Error details $fakeError")
            }

            Thread.sleep(500)

            return "Request Completed $url "
        }

    }
}
