package com.nus.team4.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.AuthenticationException;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends AuthenticationException {

//    状态码
    private Integer code;

//    错误信息
    private String errMsg;
}
