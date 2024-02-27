package com.nus.team4.exception;

import com.nus.team4.advice.Result;
import com.nus.team4.common.ResponseCode;
import com.nus.team4.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
//    只要捕获到BusinessException异常，就执行此方法
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result error(BusinessException businessException) {
        log.error("business exception: [{}]" + businessException.getErrMsg());
        return Result.error(businessException.getCode(), businessException.getErrMsg());
    }

    @ExceptionHandler(value = IOException.class)
    @ResponseBody
    public Result error(IOException ioException) {
        log.error("ioexception: [{}]" + ioException);
        return Result.error(ResponseCode.COMMON_FAIL.getCode(), "验证码生成错误");
    }
}
