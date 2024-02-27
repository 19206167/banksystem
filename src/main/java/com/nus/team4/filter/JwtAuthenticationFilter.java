package com.nus.team4.filter;

import com.nus.team4.constant.AuthorityConstant;
import com.nus.team4.util.JwtUtil;
import com.nus.team4.util.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private RedisUtil redisUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        获取token
        String token = httpServletRequest.getHeader("token");

        log.info("token: [{}]", token);

        if (token == null || token.equals("")) {
//            后面的filter会验证此request为非认证状态
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
//        解析token
        String username;
        try {
            username = JwtUtil.parseUserInfoFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("token 非法");
        }
//        从redis中获取用户信息
        log.info("jwt filter username: [{}]", username);
        StringBuilder key = new StringBuilder(AuthorityConstant.JWT_USER_INFO_KEY).append(username);
        log.info("redis key: [{}]", key);
        long expire = redisUtil.getExpire(key.toString());
        log.info("expire: [{}]" + expire);

        if (expire <= 0) {
            log.error("登录时间已过期");
            throw new ServletException("用户未登录");
        }
        String userToken = redisUtil.get(key.toString());

        log.info("redis userToken: [{}]", userToken);

        if (userToken == null) {
            // 后面的filter会验证此request为非认证状态
            throw new ServletException("用户未登录");
        }

//        存入securityContextHolder
//        权限信息封装到usernamePasswordAuthenticationToken中
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList("role"));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        log.info("jwt filter 放行");
//        放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
