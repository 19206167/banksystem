package com.nus.team4.exception;

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

import java.io.IOException;
import java.util.List;

import java.util.stream.Collectors;

import com.nus.team4.common.ResponseCode;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException");
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        // 将所有验证错误消息组合成一个字符串
        String errorMsg = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.error(400, errorMsg);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result error(BusinessException businessException) {
        log.error("business exception: [{}]", businessException.getErrMsg());
        return Result.error(businessException.getCode(), businessException.getErrMsg());
    }

    @ExceptionHandler(value = IOException.class)
    @ResponseBody
    public Result error(IOException ioException) {
        log.error("ioexception: [{}]", ioException);
        return Result.error(ResponseCode.COMMON_FAIL.getCode(), "验证码生成错误");
    }
}
