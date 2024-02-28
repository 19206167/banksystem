package com.nus.team4.service;


import com.nus.team4.advice.Result;
import com.nus.team4.vo.TransactionForm;

import java.math.BigDecimal;


public interface TransactionService {
    Result transaction(TransactionForm transactionForm);

    Result getTransactionHistory(String userId, int pageNow, int pageSize);

    Result deposit(BigDecimal amount, String iban);

    Result withdraw(BigDecimal amount, String iban);

}
