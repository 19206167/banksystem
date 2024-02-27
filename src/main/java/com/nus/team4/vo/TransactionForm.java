package com.nus.team4.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionForm {
    String senderCardNumber;
    String securityCode;
    String senderName;

    String receiverCardNumber;
    String receiverName;

    BigDecimal amount;
}
