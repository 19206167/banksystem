package com.nus.team4.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @GetMapping("/test")
    public String test() {
        log.info("调用方法：[{}]", "transaction test");
        return "test";
    }
}
