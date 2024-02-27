package com.nus.team4.mapper;


import com.nus.team4.pojo.Card;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface TransactionMapper {
    @Insert("insert into t_transaction (sender_card_number, receiver_card_number, amount) " +
            "values(#{senderCardNumber}, #{receiverCardNumber}, #{amount});")
    void insertTransactionInfo(String senderCardNumber, String receiverCardNumber, BigDecimal amount);
}
