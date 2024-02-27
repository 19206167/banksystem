package com.nus.team4.mapper;


import com.nus.team4.pojo.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TransactionMapper {
    @Insert("insert into t_transaction (sender_card_number, receiver_card_number, amount, user_id) " +
            "values(#{senderCardNumber}, #{receiverCardNumber}, #{amount}, #{userId});")
    void insertTransactionInfo(String senderCardNumber, String receiverCardNumber, BigDecimal amount, Long userId);

    @Select("Select * from t_transaction where user_id = #{userId}")
    List<Transaction> selectTransactionByPage(Long userId);
}
