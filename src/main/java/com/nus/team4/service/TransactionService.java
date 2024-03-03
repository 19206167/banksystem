package com.nus.team4.service;


import com.nus.team4.advice.Result;
import com.nus.team4.dto.request.BalanceDto;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.vo.TransactionForm;
import com.nus.team4.vo.TransactionHistoryForm;

import java.math.BigDecimal;
import java.util.Map;


public interface TransactionService {
    Result transaction(TransactionForm transactionForm) throws BusinessException;

    Result getTransactionHistory(TransactionHistoryForm transactionHistoryForm);

    Result deposit(BalanceDto balanceDto);

    Result withdraw(BalanceDto balanceDto);

    Result<Map<String, String>> getCardNumber(String token) throws Exception;
}
