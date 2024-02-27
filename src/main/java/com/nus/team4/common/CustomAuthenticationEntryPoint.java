package com.nus.team4.common;


import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        if (!response.isCommitted()) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            try (ServletOutputStream ous = response.getOutputStream()) {
                Map<String, Object> map = new HashMap<>(16);
                map.put("code", ResponseCode.COMMON_FAIL);
                map.put("success", false);
                map.put("message",e.getMessage());
                ous.write(JSON.toJSONString(map).getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}

