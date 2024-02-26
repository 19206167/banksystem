package com.nus.team4.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationForm {
    String cardNumber;

    String username;

//    md5加密的密码
    String password;
}
