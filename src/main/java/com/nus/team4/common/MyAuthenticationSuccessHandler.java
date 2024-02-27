package com.nus.team4.common;

import com.nus.team4.advice.Result;
import com.nus.team4.constant.AuthorityConstant;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.util.JwtUtil;
import com.nus.team4.util.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MyAuthenticationSuccessHandler extends JSONAuthentication implements AuthenticationSuccessHandler {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
//        get username
        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

//        generate jwt
        String jwt;
        try {
            jwt = JwtUtil.createJWT(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            this.WriteJSON(httpServletRequest, httpServletResponse,
                    Result.error(ResponseCode.COMMON_FAIL.getCode(), ResponseCode.COMMON_FAIL.getMessage()));
            throw new ServletException(ResponseCode.COMMON_FAIL.getMessage());
        }

        log.info("login success!");

        httpServletResponse.setHeader("token", jwt);
        StringBuilder key = new StringBuilder(AuthorityConstant.JWT_USER_INFO_KEY).append(user.getUsername());

        // 将jwt存入redis中，鉴权时取出, 更新过期时间
        redisUtil.set(key.toString(), jwt);
        redisUtil.expire(key.toString(), Long.valueOf(AuthorityConstant.DEFAULT_EXPIRE_MINUTE), TimeUnit.MINUTES);

        this.WriteJSON(httpServletRequest, httpServletResponse, Result.success("login success!"));
    }
}
