package com.nus.team4.controller;

import com.nus.team4.advice.Result;
import com.nus.team4.dto.BalanceDto;
import com.nus.team4.service.impl.TransactionServiceImpl;
import com.nus.team4.vo.RegistrationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionController {
    TransactionServiceImpl transactionSvc = new TransactionServiceImpl();
    @GetMapping("/test")
    public String test() {
        log.info("调用方法：[{}]", "transaction test");
        return "test";
    }

    //for test
    @PostMapping("/deposit")
    public Result<String> deposit(@RequestBody BalanceDto balanceDto) {
        log.info("调用方法： [{}]", "deposit");
        transactionSvc.deposit(balanceDto.getAmount(), balanceDto.getIban());
        return Result.success("deposit");
    }

    @PostMapping("/withdraw")
    public Result<String> withdraw(@RequestBody BalanceDto balanceDto) {
        log.info("调用方法： [{}]", "withdraw");
        transactionSvc.withdraw(balanceDto.getAmount(), balanceDto.getIban());
        return Result.success("deposit");
    }
}
