package com.nus.team4.filter;


import com.nus.team4.common.MyAuthenticationFailureHandler;
import com.nus.team4.util.RedisUtil;
import com.nus.team4.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURI();
        //当url为登录路径且请求方式为post时，进入该过滤器处理
        log.info("url: " + url);
        if ("/user/login".equals(url) && "POST".equals(httpServletRequest.getMethod())){
            try {
                validate(httpServletRequest);
            }catch (AuthenticationException exception){
                //如果不正确，扑获到验证码异常就交给认证失败处理器
                if (authenticationFailureHandler == null)
                    authenticationFailureHandler = SpringContextUtil.getBean(AuthenticationFailureHandler.class);

                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse, exception);
            }

        }
        //验证成功，则过滤链继续往下执行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    //验证码校验逻辑
    private void validate(HttpServletRequest httpServletRequest) throws AuthenticationException {
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("key");
        log.info("validate key: [{}]", key);
        log.info("validate code: [{}]", code);
        //判断是否为空
        if (code == null || code.equals("") || key == null || key.equals("")){
            throw new AuthenticationServiceException("验证码错误");
        }

        if (redisUtil == null) {
            redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        }

        if (redisUtil.getExpire(key) <= 0 || !code.equals(redisUtil.get(key))){
            throw new AuthenticationServiceException("验证码错误");
        }
        //删除缓存，一次性使用
        redisUtil.delete(key);
    }
}
