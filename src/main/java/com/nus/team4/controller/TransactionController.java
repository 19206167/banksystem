package com.nus.team4.controller;

import com.nus.team4.advice.Result;
import com.nus.team4.pojo.Transaction;
import com.nus.team4.service.TransactionService;
import com.nus.team4.vo.TransactionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/test")
    public String test() {
        log.info("调用方法：[{}]", "transaction test");
        return "test";
    }

    @PostMapping("/transfer")
    public Result transfer(@RequestBody TransactionForm transactionForm){
        return transactionService.transaction(transactionForm);

    }
}
