package com.nus.team4.controller;

import com.nus.team4.advice.Result;
import com.nus.team4.service.UserService;
import com.nus.team4.vo.JwtToken;
import com.nus.team4.vo.RegistrationForm;
import com.nus.team4.vo.UsernameAndPassword;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<JwtToken> login(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
        log.info("调用方法： [{}]", "login");
        return userService.login(usernameAndPassword);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegistrationForm registrationForm) {
        log.info("调用方法： [{}]", "register");
        return userService.register(registrationForm);
    }

    @GetMapping("/logout")
    public Result<String> logout(@RequestBody JwtToken token) throws Exception {
        log.info("调用方法：[{}]", "logout");
        return userService.logout(token.getToken());
    }
}
