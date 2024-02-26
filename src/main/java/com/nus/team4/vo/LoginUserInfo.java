package com.nus.team4.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserInfo {
    //    用户表主键id
    private Long id;

    //    用户名
    private String username;
}
