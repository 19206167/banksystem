package com.nus.team4.controller;

import com.nus.team4.advice.Result;
import com.nus.team4.annotation.InterfaceLimit;
import com.nus.team4.dto.request.BalanceDto;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.pojo.Transaction;
import com.nus.team4.service.TransactionService;
import com.nus.team4.vo.TransactionForm;
import com.nus.team4.vo.TransactionHistoryForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"/transaction", "/apis/transaction"})
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @InterfaceLimit
    @GetMapping("/getCardNumber")
    public Result<Map<String, String>> getCardNumber(HttpServletRequest request) throws Exception {
        return transactionService.getCardNumber(request.getParameter("token"));
    }


    @InterfaceLimit
    @PostMapping("/transfer")
    public Result transfer(@RequestBody TransactionForm transactionForm) throws BusinessException {
        return transactionService.transaction(transactionForm);
    }

    @InterfaceLimit
    @GetMapping("/history")
    public Result<List<Transaction>> history(@RequestBody TransactionHistoryForm transactionHistoryForm) {
        return transactionService.getTransactionHistory(transactionHistoryForm);
    }

    @InterfaceLimit
    @PostMapping("/deposit")
    public Result<String> deposit(@Valid @RequestBody BalanceDto balanceDto) {
        log.info("调用方法： [{}]", "deposit");
        transactionService.deposit(balanceDto);
        return Result.success("deposit");
    }

    @InterfaceLimit
    @PostMapping("/withdraw")
    public Result<String> withdraw(@RequestBody BalanceDto balanceDto) {
        log.info("调用方法： [{}]", "withdraw");
        transactionService.withdraw(balanceDto);
        return Result.success("deposit");
    }
}
