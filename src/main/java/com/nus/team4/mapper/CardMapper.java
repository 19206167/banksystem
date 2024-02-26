package com.nus.team4.mapper;


import com.nus.team4.pojo.Card;
import org.apache.ibatis.annotations.Select;

public interface CardMapper {
    @Select("Select * from t_card where card_number = #{cardNumber}")
    Card findByCardNumber(String cardNumber);
}
