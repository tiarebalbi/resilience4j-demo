package com.tiarebalbi.resilience.bulkhead

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bank")
class BankController(private val bankService: BankService) {

    @PostMapping("/cash-out")
    fun cashOut(@RequestBody transaction: Transaction) {
        this.bankService.cashOut(transaction.amount)
    }

    @PostMapping("/deposit")
    fun deposit(@RequestBody transaction: Transaction) {
        this.bankService.deposit(transaction.amount)
    }
}