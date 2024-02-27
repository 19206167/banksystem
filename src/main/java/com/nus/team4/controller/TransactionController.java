package com.nus.team4.controller;

import com.nus.team4.advice.Result;
import com.nus.team4.dto.request.BalanceDto;
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

    //for test
    @PostMapping("/deposit")
    public Result<String> deposit(@RequestBody BalanceDto balanceDto) {
        log.info("调用方法： [{}]", "deposit");
        transactionService.deposit(balanceDto.getAmount(), balanceDto.getIban());
        return Result.success("deposit");
    }

    @PostMapping("/withdraw")
    public Result<String> withdraw(@RequestBody BalanceDto balanceDto) {
        log.info("调用方法： [{}]", "withdraw");
        transactionService.withdraw(balanceDto.getAmount(), balanceDto.getIban());
        return Result.success("deposit");
    }
}
