package com.nus.team4.service.impl;

import com.nus.team4.advice.Result;
import com.nus.team4.mapper.CardMapper;
import com.nus.team4.mapper.TransactionMapper;
import com.nus.team4.pojo.Card;
import com.nus.team4.service.TransactionService;
import com.nus.team4.vo.TransactionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    //    转账是事务的，保证一致性
    @Transactional
    @Override
    public Result transaction(TransactionForm transactionForm) {

//        首先得到发送者账户和接受者账户
        Card senderCard = cardMapper.findByCardNumber(transactionForm.getSenderCardNumber());
        Card receiverCard = cardMapper.findByCardNumber(transactionForm.getReceiverCardNumber());

        //        比较信息是否正确
        if (senderCard == null || receiverCard == null) {
            log.error("卡号错误");
            return Result.error(0, "卡号错误");
        }

        if (!senderCard.getName().equals(transactionForm.getSenderName()) ||
                !senderCard.getSecurityCode().equals(transactionForm.getSecurityCode())) {
            log.error("sender账户信息错误");
            return Result.error(0,"sender账户信息错误");
        }

        if (!receiverCard.getName().equals(transactionForm.getReceiverName())) {
            log.error("receiver 信息错误");
            return Result.error(0, "receiver 信息错误");
        }

        if (senderCard.getBalance().compareTo(transactionForm.getAmount()) < 0) {
            log.error("账户余额不足");
            return Result.error(0, "账户余额不足");
        }

//        执行转账逻辑
        senderCard.setBalance(senderCard.getBalance().subtract(transactionForm.getAmount()));
        cardMapper.updateCardBalance(senderCard.getIban(), senderCard.getBalance());

        receiverCard.setBalance(receiverCard.getBalance().add(transactionForm.getAmount()));
        cardMapper.updateCardBalance(receiverCard.getIban(), receiverCard.getBalance());

        transactionMapper.insertTransactionInfo(transactionForm.getSenderCardNumber(),
                transactionForm.getReceiverCardNumber(), transactionForm.getAmount());

        return Result.success("转账成功");
    }
}

