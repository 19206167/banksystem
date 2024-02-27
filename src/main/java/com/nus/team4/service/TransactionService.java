package com.nus.team4.service;

import java.math.BigDecimal;

public interface TransactionService {
    void deposit(BigDecimal amount, String iban);

    void withdraw(BigDecimal amount, String iban);
}
