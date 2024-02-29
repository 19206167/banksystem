package com.nus.team4;

import com.nus.team4.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching // 开启缓存注解功能
@ComponentScan({"com.nus.team4.config", "com.nus.team4.util", "com.nus.team4.service.impl",
        "com.nus.team4.controller", "com.nus.team4.exception"})
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MainApplication.class, args);
        SpringContextUtil.setApplicationContext(ctx);
    }
}
