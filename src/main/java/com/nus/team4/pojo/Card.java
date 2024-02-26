package com.nus.team4.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    /** 自增id */
    private Long id;

    /** 卡号 **/
    private String iban;

    private String currency;

    private String status;

    private String accountType;

    /** 三位安全码 **/
    private String SecurityCode;

    /** 账户余额 **/
    private BigDecimal balance;

    /** email **/
    private String email;

    /** 用户姓名 **/
    private String name;

    /** 电话号码 **/
    private String phone;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
