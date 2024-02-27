package com.nus.team4.common;

import com.nus.team4.advice.Result;
import com.nus.team4.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result  handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException" );
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        // 将所有验证错误消息组合成一个字符串
        String errorMsg = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.error(400, errorMsg);
    }
    
//    只要捕获到BusinessException异常，就执行此方法
    public Result error(BusinessException businessException) {
        log.error("business exception: [{}]" + businessException.getErrMsg());
        return Result.error(businessException.getCode(), businessException.getErrMsg());
    }


}
