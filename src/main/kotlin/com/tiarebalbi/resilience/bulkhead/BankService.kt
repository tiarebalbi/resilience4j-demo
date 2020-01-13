package com.tiarebalbi.resilience.bulkhead

import com.tiarebalbi.resilience.retry.BankTransactionException
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import java.math.BigDecimal

@Component
@Bulkhead(name = "bankService")
class BankService {

    private final val logger = LoggerFactory.getLogger(BankService::class.java)

    private final var currentBalance = BigDecimal(1_000) // Initial Balance

    @Retry(name = "bankService", fallbackMethod = "recoverCashOut")
    fun cashOut(total: BigDecimal) {
        if (currentBalance < total) {
            throw BankTransactionException("No balance for this transaction")
        }

        logger.info("Cash out of $total€")
        this.currentBalance = this.currentBalance - total
        sleep(1500) // fake pause
        logger.info("Done, new balance is: ${this.currentBalance}€")
    }

    @Retry(name = "bankService", fallbackMethod = "recoverDeposit")
    fun deposit(total: BigDecimal) {
        logger.info("Processing deposit")
        this.currentBalance = this.currentBalance + total
        sleep(1000)
        logger.info("Deposit completed. New balance ${this.currentBalance}€")
    }

    fun recoverCashOut(total: BigDecimal, exception: BankTransactionException) {
        logger.error("Request rejected. Total requested $total, details: ${exception.message}")
    }

    fun recoverDeposit(total: BigDecimal, exception: Exception) {
        logger.info("Saving deposit to be processed later. Total of $total")
    }
}