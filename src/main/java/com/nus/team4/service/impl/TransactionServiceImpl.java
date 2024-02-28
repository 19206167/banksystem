package com.nus.team4.service.impl;

import com.nus.team4.advice.Result;
import com.nus.team4.dto.request.BalanceDto;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.mapper.CardMapper;
import com.nus.team4.mapper.TransactionMapper;
import com.nus.team4.mapper.UserMapper;
import com.nus.team4.pojo.Card;
import com.nus.team4.pojo.Transaction;
import com.nus.team4.pojo.User;
import com.nus.team4.service.TransactionService;
import com.nus.team4.vo.TransactionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nus.team4.common.ResponseCode;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserMapper userMapper;

    //    转账是事务的，保证一致性
    @Transactional
    @Override
    public Result transaction(TransactionForm transactionForm) throws BusinessException {

//        首先得到发送者账户和接受者账户
        Card senderCard = cardMapper.findByCardNumber(transactionForm.getSenderCardNumber());
        Card receiverCard = cardMapper.findByCardNumber(transactionForm.getReceiverCardNumber());

        //        比较信息是否正确
        if (senderCard == null || receiverCard == null) {
            log.error("卡号错误");
            throw new BusinessException(ResponseCode.CARD_NUMBER_WRONG.getCode(), ResponseCode.CARD_NUMBER_WRONG.getMessage());
        }

        if (!senderCard.getName().equals(transactionForm.getSenderName()) ||
                !senderCard.getSecurityCode().equals(transactionForm.getSecurityCode())) {
            log.error("sender账户信息错误");
            throw new BusinessException(ResponseCode.CARD_NAME_WRONG.getCode(), "sender" + ResponseCode.CARD_NAME_WRONG.getMessage());
        }

        if (!receiverCard.getName().equals(transactionForm.getReceiverName())) {
            log.error("receiver 信息错误");
            throw new BusinessException(ResponseCode.CARD_NAME_WRONG.getCode(), "receiver" + ResponseCode.CARD_NAME_WRONG.getMessage());
        }

        if (senderCard.getBalance().compareTo(transactionForm.getAmount()) < 0) {
            log.error("账户余额不足");
            throw new BusinessException(ResponseCode.CARD_NO_BALANCE.getCode(), ResponseCode.CARD_NO_BALANCE.getMessage());
        }

//        执行转账逻辑
        senderCard.setBalance(senderCard.getBalance().subtract(transactionForm.getAmount()));
        cardMapper.updateCardBalance(senderCard.getIban(), senderCard.getBalance());

        receiverCard.setBalance(receiverCard.getBalance().add(transactionForm.getAmount()));
        cardMapper.updateCardBalance(receiverCard.getIban(), receiverCard.getBalance());

        transactionMapper.insertTransactionInfo(transactionForm.getSenderCardNumber(),
                transactionForm.getReceiverCardNumber(), transactionForm.getAmount(),
                userMapper.findByCardId(senderCard.getId()).getId());

        return Result.success("转账成功");
    }

    @Override
    public Result getTransactionHistory(String username, int pageNow, int pageSize) {
        log.info(username);

        User user = userMapper.findByUsername("lzj");

        log.info("transaction history user: [{}]", user);

        PageHelper.startPage(pageNow, pageSize);

        List<Transaction> transactions = transactionMapper.selectTransactionByPage(user.getId());

        log.info("transactions: [{}]", transactions);

        return Result.success(transactions, "get transaction history.");
    }

    @Transactional
    @Override
    public Result deposit(BalanceDto balanceDto){
        String iban = balanceDto.getIban();
        BigDecimal amount = balanceDto.getAmount();
        long userId = balanceDto.getUserId();

        Card card = cardMapper.findByCardNumber(iban);

        if (card == null) {
            log.error("卡号错误");
            return Result.error(0, "卡号错误");
        }

        card.setBalance(card.getBalance().add(amount));
        cardMapper.updateCard(card);
        transactionMapper.insertTransactionInfo("111",
                iban, amount, userId);
        return Result.success("deposit success");
    }

    @Transactional
    @Override
    public Result withdraw(BalanceDto balanceDto){
        String iban = balanceDto.getIban();
        BigDecimal amount = balanceDto.getAmount();
        long userId = balanceDto.getUserId();

        Card card = cardMapper.findByCardNumber(iban);

        if (card == null) {
            log.error("卡号错误");
            return Result.error(0, "卡号错误");
        }

        if (card.getBalance().compareTo(amount) < 0) {
            log.error("账户余额不足");
            return Result.error(0, "账户余额不足");
        }

        card.setBalance(card.getBalance().subtract(amount));
        cardMapper.updateCard(card);

        transactionMapper.insertTransactionInfo(iban,
                "222", amount, userId);
        return Result.success("withdraw success");
    }


}

