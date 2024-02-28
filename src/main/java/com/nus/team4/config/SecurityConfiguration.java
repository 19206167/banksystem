package com.nus.team4.config;


import com.nus.team4.common.CustomAuthenticationEntryPoint;
import com.nus.team4.filter.CaptchaFilter;
import com.nus.team4.filter.JwtAuthenticationFilter;
import com.nus.team4.common.MyAuthenticationFailureHandler;
import com.nus.team4.common.MyAuthenticationSuccessHandler;
import com.nus.team4.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new MyAuthenticationSuccessHandler();
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler(){
        return new MyAuthenticationFailureHandler();
    }

    /**
     * jwt过滤器
     * @return
     * @throws Exception
     */
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

//    验证码过滤器
    CaptchaFilter captchaFilter(){
        return new CaptchaFilter();
    }

    @Bean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        首页所有人可以访问，功能页需要登录之后才能访问
//        链式编程

//        添加jwt过滤器, 添加到usernamePasswordFilter之前
        http.addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter());

        http.authorizeRequests().antMatchers("/user/**").permitAll()
//                .antMatchers("/transaction/**").permitAll()
                .anyRequest().authenticated()
//                登录
                .and().formLogin().loginProcessingUrl("/user/login")
//                登录成功，失败处理器
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
//                登出
                .and().logout().logoutUrl("/user/logout")
//                登出退回主界面, 路径可更改
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .and().exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint());

        //关闭CSRF跨域
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
