package com.nus.team4.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.nus.team4.advice.Result;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.service.UserService;
import com.nus.team4.dto.request.AccountOpenForm;
import com.nus.team4.util.RedisUtil;
import com.nus.team4.vo.JwtToken;
import com.nus.team4.vo.RegistrationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

//    @PostMapping("/login")
//    public Result<JwtToken> login(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
//        log.info("调用方法： [{}]", "login");
//        return userService.login(usernameAndPassword);
//    }

    @PostMapping("/openAccount")
    public Result openAccount(@Valid @RequestBody AccountOpenForm accountOpenForm) throws Exception {
        log.info("调用方法： [{}]", "openAccount");
        return userService.openAccount(accountOpenForm);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegistrationForm registrationForm) throws BusinessException {
        log.info("调用方法： [{}]", "register");
        return userService.register(registrationForm);
    }

    @GetMapping("/logout")
    public Result<String> logout(@RequestBody JwtToken token) throws Exception {
        log.info("调用方法：[{}]", "logout");
//        return Result.success("logout");
        return userService.logout(token.getToken());
    }

    /*获取验证码，借助hutool的验证码生成工具类*/
    @GetMapping("/getCaptcha")
    public Result<Map<String, String>> getCode(HttpServletResponse response) throws IOException {
        //生成随机码，作为验证码的key值，传给前端（方便验证时，根据key从redis中取出正确的验证码value）
        String key = UUID.randomUUID().toString();

        // 随机生成宽200、高100的 4 位验证码
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        String code = captcha.getCode();
        log.info("key: [{}]", key);
        log.info("code: [{}]", code);

        //写入到流中
        BufferedImage bufferedImage = captcha.getImage();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        //进行base64编码
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //编码前缀
        String str = "data:image/jpeg;base64,";
        //使用hutool自己提供的方法，直接获取base64编码
//        String base64 = str + captcha.getImageBase64();
        String base64Image = str + base64Encoder.encode(outputStream.toByteArray());
        //将验证码和对应的随机key值写入缓存数据库
        redisUtil.set(key, code, 600l, TimeUnit.SECONDS);

        Map<String, String> res = new HashMap<>();
        res.put("key", key);
        res.put("image", base64Image);
//        response.setContentType("image/jpeg");
//        response.setHeader("Pragma", "No-cache");
//        Map<String, String> map = new HashMap<>();

//        captcha.write(response.getOutputStream());

//        response.getOutputStream().write("verificationCode", res);
//        response.getOutputStream().close();
        return Result.success(res, "验证码");
    }
}
