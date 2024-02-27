package com.nus.team4.controller;

import com.nus.team4.advice.Result;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.mapper.UserMapper;
import com.nus.team4.pojo.Transaction;
import com.nus.team4.service.TransactionService;
import com.nus.team4.vo.TransactionForm;
import com.nus.team4.vo.TransactionHistoryForm;
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
//        log.info("user: [{}]", userMapper.findByUsername("lzj"));
        return "test";
    }

    @PostMapping("/transfer")
    public Result transfer(@RequestBody TransactionForm transactionForm) throws BusinessException {
        return transactionService.transaction(transactionForm);
    }

    @GetMapping("/transactionHistory")
    public Result transactionHistory(@RequestBody TransactionHistoryForm transactionHistoryForm) {
        return transactionService.getTransactionHistory(transactionHistoryForm.getUsername(),
                transactionHistoryForm.getPageNum(), transactionHistoryForm.getPageSize());
    }
}
