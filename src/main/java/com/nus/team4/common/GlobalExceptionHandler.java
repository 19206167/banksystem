package com.nus.team4.common;

import com.nus.team4.advice.Result;
import com.nus.team4.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
//    只要捕获到BusinessException异常，就执行此方法
    public Result error(BusinessException businessException) {
        log.error("business exception: [{}]" + businessException.getErrMsg());
        return Result.error(businessException.getCode(), businessException.getErrMsg());
    }
}
