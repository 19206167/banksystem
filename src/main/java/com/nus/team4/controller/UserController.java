package com.nus.team4.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.nus.team4.advice.Result;
import com.nus.team4.annotation.InterfaceLimit;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.service.EmailService;
import com.nus.team4.service.UserService;
import com.nus.team4.dto.request.AccountOpenForm;
import com.nus.team4.service.impl.EmailServiceImpl;
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
@RequestMapping({"/user", "/apis/user"})
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailServiceImpl emailService;

//    @PostMapping("/login")
//    public Result<JwtToken> login(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception {
//        log.info("调用方法： [{}]", "login");
//        return userService.login(usernameAndPassword);
//    }

    @InterfaceLimit
    @PostMapping("/openAccount")
    public Result openAccount(@Valid @RequestBody AccountOpenForm accountOpenForm) throws Exception {
        log.info("调用方法： [{}]", "openAccount");
        return userService.openAccount(accountOpenForm);
    }

    @InterfaceLimit
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

    @InterfaceLimit
    @GetMapping("/getIban")
    public Result<String> getIban(@RequestBody JwtToken token) throws Exception {
        log.info("调用方法：[{}]", "getIban");
        return userService.getIban(token.getToken());
    }

    @InterfaceLimit
    @GetMapping("/getName")
    public Result<String> getName(@RequestBody JwtToken token) throws Exception {
        log.info("调用方法：[{}]", "getName");
        return userService.getName(token.getToken());
    }

    @InterfaceLimit
    @GetMapping("/emailTest")
    public void emailTest()  {
        // 发送邮件通知
        emailService.sendSimpleMessage(
                "515118458@qq.com",
                "银行操作通知",
                "亲爱的用户，您的账户有新的操作，请检查。"
        );
    }

    /*获取验证码，借助hutool的验证码生成工具类*/
//    使用次數限制
    @InterfaceLimit
    @GetMapping("/getCaptcha")
    @CrossOrigin(origins = "http://localhost:5173")
    public void getCode(HttpServletResponse response) throws IOException {
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

        // 在响应头中添加自定义字段"Captcha-Key"来传递验证码的key
        response.setHeader("Captcha-Key", key);

        // 设置响应内容类型为图像JPEG
        response.setContentType("image/jpeg");

        // 禁用缓存
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Expires", "0");

        // 将验证码图像作为二进制流发送到响应体
        captcha.write(response.getOutputStream());
    }
}
