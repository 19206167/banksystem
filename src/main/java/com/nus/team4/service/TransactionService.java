package com.nus.team4.service;

import java.math.BigDecimal;
import com.nus.team4.advice.Result;
import com.nus.team4.vo.TransactionForm;

public interface TransactionService {
    Result deposit(BigDecimal amount, String iban);

    Result withdraw(BigDecimal amount, String iban);

    Result transaction(TransactionForm transactionForm);
}
