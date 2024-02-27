package com.nus.team4.service;


import java.math.BigDecimal;

public interface TransactionService {
    void transaction(String senderCardNumber, String receiverCardNumber, BigDecimal amount);
}
