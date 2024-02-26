package com.nus.team4.mapper;


import com.nus.team4.pojo.Card;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CardMapper {
    @Select("Select * from t_card where card_number = #{cardNumber}")
    Card findByCardNumber(String cardNumber);

    // 插入新卡信息
    @Insert("INSERT INTO t_card (cardNumber, SecurityCode, amount, email, name, phone, createTime, updateTime) VALUES (#{cardNumber}, #{SecurityCode}, #{amount}, #{email}, #{name}, #{phone}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCard(Card card);

    // 更新卡信息
    @Update("UPDATE t_card SET SecurityCode=#{SecurityCode}, amount=#{amount}, email=#{email}, name=#{name}, phone=#{phone}, updateTime=#{updateTime} WHERE cardNumber = #{cardNumber}")
    int updateCard(Card card);

    // 删除卡信息
    @Delete("DELETE FROM t_card WHERE cardNumber = #{cardNumber}")
    int deleteByCardNumber(String cardNumber);

    // 查询所有卡信息
    @Select("SELECT * FROM t_card")
    List<Card> findAll();
}
