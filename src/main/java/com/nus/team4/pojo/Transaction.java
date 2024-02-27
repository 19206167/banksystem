package com.nus.team4.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;

    private String senderCardNumber;

    private String receiverCardNumber;

    private BigDecimal amount;

    private Boolean deleted = false;

    private Date createdTime;

    private Date updatedTime;
}
