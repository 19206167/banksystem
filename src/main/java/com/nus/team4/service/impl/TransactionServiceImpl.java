package com.nus.team4.service.impl;

import com.nus.team4.mapper.CardMapper;
import com.nus.team4.pojo.Card;
import com.nus.team4.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private CardMapper cardMapper;
    public void deposit(BigDecimal amount, String iban){
        Card card = cardMapper.findByCardNumber(iban);
        card.setBalance(card.getBalance().add(amount));
        cardMapper.updateCard(card);
    }

    public void withdraw(BigDecimal amount, String iban){
        Card card = cardMapper.findByCardNumber(iban);
        card.setBalance(card.getBalance().subtract(amount));
        cardMapper.updateCard(card);
    }


}
