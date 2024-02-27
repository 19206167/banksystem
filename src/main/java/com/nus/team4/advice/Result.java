package com.nus.team4.advice;

import com.nus.team4.common.ResponseCode;

import java.io.Serializable;

/**
 * 后端统一返回结果
 */
public class Result<T> implements Serializable {


    //    编码，200成功，其它数字为失败
    private Integer code;
//    错误信息
    private String msg;
//    数据
    private T data;

    public static <T> Result<T> success(String msg){
        Result<T> result = new Result<>();
        result.code = ResponseCode.SUCCESS.getCode();
        if (msg == null || msg.equals("")) {
            result.msg = ResponseCode.SUCCESS.getMessage();
        } else {
            result.msg = msg;
        }
        return result;
    }

    public static <T> Result<T> success(T data, String msg) {
        Result<T> result = new Result<>();
        result.data = data;
        result.code = ResponseCode.SUCCESS.getCode();
        if (msg == null || msg.equals("")) {
            result.msg = ResponseCode.SUCCESS.getMessage();
        } else {
            result.msg = msg;
        }
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        return result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
