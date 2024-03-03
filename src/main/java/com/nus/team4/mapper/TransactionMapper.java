package com.nus.team4.mapper;


import com.nus.team4.pojo.Transaction;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface TransactionMapper {
    @Insert("insert into t_transaction (sender_card_number, receiver_card_number, amount, user_id) " +
            "values(#{senderCardNumber}, #{receiverCardNumber}, #{amount}, #{userId});")
    void insertTransactionInfo(String senderCardNumber, String receiverCardNumber, BigDecimal amount, Long userId);

    @Select("Select * from t_transaction where user_id = #{userId}")
    List<Transaction> selectTransactionByPage(Long userId);

    @Select("SELECT * FROM t_transaction " +
            "WHERE user_id = #{userId} " +
            "AND created_time BETWEEN #{startTime} AND #{endTime} " +
            "AND deleted = false")
    List<Transaction> selectTransactionsByUserAndDates(
            @Param("userId") Long userId,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime);
}
