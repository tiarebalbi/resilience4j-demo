package com.tiarebalbi.resilience.retry

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
class BankTransactionException(msg: String) : RuntimeException(msg)