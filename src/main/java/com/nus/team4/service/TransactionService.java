package com.nus.team4.service;


import com.nus.team4.advice.Result;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.vo.TransactionForm;


public interface TransactionService {
    Result transaction(TransactionForm transactionForm) throws BusinessException;
}
