package com.nus.team4.common;

import com.nus.team4.advice.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyAuthenticationFailureHandler extends JSONAuthentication implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //继承封装的输出JSON格式类，并调用父类方法即可
        this.WriteJSON(httpServletRequest,httpServletResponse, Result.error(ResponseCode.COMMON_FAIL.getCode(), e.getMessage()));
    }
}
