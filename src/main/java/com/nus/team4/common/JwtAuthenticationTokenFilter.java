package com.nus.team4.common;

import com.nus.team4.exception.BusinessException;
import com.nus.team4.pojo.User;
import com.nus.team4.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, Exception {
        String requestUrl = request.getRequestURI();
        String authToken = request.getHeader(jwtConfig.getHeader());
        String userName = null;
        if (authToken != null) {
            userName = jwtUtil.parseUserInfoFromToken(authToken).getUsername();
        }

        log.info("进入jwt自定义token过滤器");
        log.info("自定义token过滤器获得用户名为：" + userName);

        //当userName不为空时进行校验token是否为有效token
        //ObjectUtil.isNotEmpty()和ObjectUtil.isNull()是hutool中的方法。
        /*
            前者意思是指对象是否不为空，和isNotNull()不同。
            比如""，isNotNull()会返回true而isNotEmpty()会返回false。
            userName是字符串所以使用isNotEmpty()，该方法也很适合集合判空
        */
        /*
            getAuthentication()使用isNull()原因是:
            通过前面几个代码块的代码，可以看出是存储授权信息的
            这里的意思是如果用户名不为空并且授权信息又有值，那么就直接跳过，反之就是进入下面的if内部
        */
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            User user =
            //检验token
            if (!jwtUtil.verifyToken(authToken)) {
                throw new BusinessException(500,"token已过期");
            }else if (userName.equals(user.getUsername())){
                /**
                 * UsernamePasswordAuthenticationToken继承AbstractAuthenticationToken实现Authentication
                 * 所以当在页面中输入用户名和密码之后首先会进入到UsernamePasswordAuthenticationToken验证(Authentication)，
                 * 然后生成的Authentication会被交由AuthenticationManager来进行管理
                 * 而AuthenticationManager管理一系列的AuthenticationProvider，
                 * 而每一个Provider都会通UserDetailsService和UserDetail来返回一个
                 * 以UsernamePasswordAuthenticationToken实现的带用户名和密码以及权限的Authentication
                 */
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //将authentication放入SecurityContextHolder中
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }

}
