package com.nus.team4.mapper;


import com.nus.team4.pojo.Card;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CardMapper {
    @Select("Select * from t_card where iban = #{iban}")
    Card findByCardNumber(String iban);

    @Select("Select * from t_card where id = #{id}")
    Card findByCardID(Long id);

    // 插入新卡信息
    @Insert("INSERT INTO t_card (iban, SecurityCode, balance, email, name, phone, currency, accounttype, status, create_time, update_time) VALUES (#{iban}, #{SecurityCode}, #{balance}, #{email}, #{name}, #{phone}, #{currency}, #{status}, #{accountType}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertCard(Card card);

    //   更新钱
    @Update("UPDATE t_card SET balance=#{newBalance} where iban=#{cardNumber}")
    int updateCardBalance(String cardNumber, BigDecimal newBalance);

    // 更新卡信息
    @Update("UPDATE t_card SET SecurityCode=#{SecurityCode}, balance=#{balance}, email=#{email}, name=#{name}, phone=#{phone}, update_time=#{updateTime} WHERE iban = #{iban}")
    int updateCard(Card card);

    // 删除卡信息
    @Delete("DELETE FROM t_card WHERE cardNumber = #{iban}")
    int deleteByCardNumber(String cardNumber);

    // 查询所有卡信息
    @Select("SELECT * FROM t_card")
    List<Card> findAll();
}
