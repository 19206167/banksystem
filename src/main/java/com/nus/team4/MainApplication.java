package com.nus.team4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching // 开启缓存注解功能
@ComponentScan({"com.nus.team4.config", "com.nus.team4.util", "com.nus.team4.service.impl", "com.nus.team4.controller"})
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
