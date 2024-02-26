package com.nus.team4.controller;

import com.nus.team4.service.UserService;
import com.nus.team4.vo.JwtToken;
import com.nus.team4.vo.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public JwtToken login(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
        log.info("调用login方法！");
        return new JwtToken(userService.login(usernameAndPassword));
    }
}
