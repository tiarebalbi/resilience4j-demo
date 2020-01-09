package com.tiarebalbi.resilience.bulkhead

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import java.math.BigDecimal

@Component
@Bulkhead(name = "bankService")
class BankService {

    private final val logger = LoggerFactory.getLogger(BankService::class.java)

    private final var currentBalance = BigDecimal(1_000) // Initial Balance

    fun cashOut(total: BigDecimal) {
        if (currentBalance < total) {
            throw RuntimeException("No balance for this transaction")
        }

        logger.info("Cash out of $total€")
        this.currentBalance = this.currentBalance - total
        sleep(3000) // fake pause
        logger.info("Done, new balance is: ${this.currentBalance}")
    }

    fun deposit(total: BigDecimal) {
        this.currentBalance = this.currentBalance + total
        sleep(5000)
        logger.info("New Balance ${this.currentBalance}€")
    }
}