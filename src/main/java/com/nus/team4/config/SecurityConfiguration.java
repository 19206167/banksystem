package com.nus.team4.config;


import com.nus.team4.common.CustomAuthenticationEntryPoint;
import com.nus.team4.filter.MyUsernamePasswordAuthenticationFilter;
import com.nus.team4.filter.CaptchaFilter;
import com.nus.team4.filter.JwtAuthenticationFilter;
import com.nus.team4.common.MyAuthenticationFailureHandler;
import com.nus.team4.common.MyAuthenticationSuccessHandler;
import com.nus.team4.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // 允许来自前端服务器的域名
        configuration.setAllowedMethods(Arrays.asList("GET","POST","HEAD","OPTIONS","PUT","PATCH","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // 如果需要的话允许凭证

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 应用于所有的路径
        return source;
    }
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
    @Bean
    CaptchaFilter captchaFilter(){
        return new CaptchaFilter();
    }

    @Bean
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

//    自定义username, password过滤器
    @Bean
    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(){
        MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter = new MyUsernamePasswordAuthenticationFilter();

        myUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        myUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        myUsernamePasswordAuthenticationFilter.setFilterProcessesUrl("/user/login");
        myUsernamePasswordAuthenticationFilter.setUsernameParameter("username");
        myUsernamePasswordAuthenticationFilter.setUsernameParameter("password");
        return myUsernamePasswordAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter())
                .addFilter(usernamePasswordAuthenticationFilter())
                .authorizeRequests()
                .antMatchers("/apis/user/**").permitAll()
                .antMatchers("/user/**").permitAll()
                //.antMatchers("/transaction/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .and()
                .csrf().disable();
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
