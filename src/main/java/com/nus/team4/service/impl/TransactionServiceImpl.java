package com.nus.team4.service.impl;

import com.nus.team4.service.TransactionService;

import javax.transaction.Transactional;
import java.math.BigDecimal;


public class TransactionServiceImpl implements TransactionService {

//    转账是事务的，保证一致性

    @Transactional
    @Override
    public void transaction(String senderCardNumber, String receiverCardNumber, BigDecimal amount) {

    }
}
