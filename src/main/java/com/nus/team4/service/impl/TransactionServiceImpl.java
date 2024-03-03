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
import com.nus.team4.util.EncryptionUtil;
import com.nus.team4.util.JwtUtil;
import com.nus.team4.vo.TransactionForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import com.nus.team4.common.ResponseCode;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private EncryptionUtil encryptionUtil;


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

        /* send the notification */
        String senderEmail = encryptionUtil.decrypt(senderCard.getEmail());
        String receiverEmail = encryptionUtil.decrypt(receiverCard.getEmail());

        // sender email
        String senderMessage = String.format(
                "Dear %s,\n\n" +
                        "We wish to inform you that a transaction has been made from your bank account. Here are the details:\n\n" +
                        "- Recipient's Name: %s\n" +
                        "- Recipient's Account Number (last four digits): %s\n" +
                        "- Transaction Amount: %s\n" +
                        "- Transaction Date and Time: %s\n\n" +
                        "If you believe this was a mistake or an unauthorized transaction, please contact us immediately.\n\n" +
                        "Thank you,\n" +
                        "Your Bank's Customer Service Team",
                senderCard.getName(),
                receiverCard.getName(),
                transactionForm.getReceiverCardNumber().substring(transactionForm.getReceiverCardNumber().length() - 4),
                transactionForm.getAmount().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        try {
            emailService.sendSimpleMessage(
                    encryptionUtil.decrypt(senderCard.getEmail()),
                    "Bank Transaction Notification",
                    senderMessage
            );
        } catch (MailException e) {
            // 日志记录邮件发送失败
            log.error("Failed to send email to sender: " + e.getMessage());
        }

        // receiver email
        String receiverMessage = String.format(
                "Dear %s,\n\n" +
                        "We are pleased to inform you that your bank account has received a transfer. Here are the details:\n\n" +
                        "- Sender's Name: %s\n" +
                        "- Sender's Account Number (last four digits): %s\n" +
                        "- Transaction Amount: %s\n" +
                        "- Transaction Date and Time: %s\n\n" +
                        "Please log in to your online banking account or contact us for more information.\n\n" +
                        "Thank you,\n" +
                        "Your Bank's Customer Service Team",
                receiverCard.getName(),
                senderCard.getName(),
                transactionForm.getSenderCardNumber().substring(transactionForm.getSenderCardNumber().length() - 4),
                transactionForm.getAmount().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        try {
            emailService.sendSimpleMessage(
                    encryptionUtil.decrypt(receiverCard.getEmail()),
                    "Bank Credit Notification",
                    receiverMessage
            );
        } catch (MailException e) {
            // 日志记录邮件发送失败
            log.error("Failed to send email to receiver: " + e.getMessage());
        }


        return Result.success("transaction success");
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
        Card card = cardMapper.findByCardNumber(iban);
        BigDecimal amount = balanceDto.getAmount();
        long userId = userMapper.findByCardId(card.getId()).getId();

        if (card == null) {
            log.error("卡号错误");
            return Result.error(0, "卡号错误");
        }

        card.setBalance(card.getBalance().add(amount));
        cardMapper.updateCard(card);
        transactionMapper.insertTransactionInfo("111",
                iban, amount, userId);

        String depositMessage = String.format(
                "Dear Customer,\n\n" +
                        "A deposit transaction has been successfully credited to your account. Here are the details:\n\n" +
                        "- Account IBAN: %s\n" +
                        "- Deposit Amount: %s\n" +
                        "- New Balance: %s\n" +
                        "- Transaction Date and Time: %s\n\n" +
                        "If you have any questions or did not authorize this transaction, please contact us immediately.\n\n" +
                        "Thank you,\n" +
                        "Your Bank's Customer Service Team",
                iban,
                amount.toString(),
                card.getBalance().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        try {
            emailService.sendSimpleMessage(
                    encryptionUtil.decrypt(card.getEmail()),
                    "Deposit Notification",
                    depositMessage
            );
        } catch (MailException e) {
            log.error("Failed to send deposit email notification: " + e.getMessage());
        }

        return Result.success("deposit success");
    }

    @Transactional
    @Override
    public Result withdraw(BalanceDto balanceDto){
        String iban = balanceDto.getIban();
        Card card = cardMapper.findByCardNumber(iban);
        BigDecimal amount = balanceDto.getAmount();
        long userId = userMapper.findByCardId(card.getId()).getId();

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

        String withdrawalMessage = String.format(
                "Dear Customer,\n\n" +
                        "A withdrawal transaction has been successfully completed from your account. Here are the details:\n\n" +
                        "- Account IBAN: %s\n" +
                        "- Withdrawal Amount: %s\n" +
                        "- New Balance: %s\n" +
                        "- Transaction Date and Time: %s\n\n" +
                        "If you have any questions or did not authorize this transaction, please contact us immediately.\n\n" +
                        "Thank you,\n" +
                        "Your Bank's Customer Service Team",
                iban,
                amount.toString(),
                card.getBalance().toString(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        try {
            emailService.sendSimpleMessage(
                    encryptionUtil.decrypt(card.getEmail()),
                    "Withdrawal Notification",
                    withdrawalMessage
            );
        } catch (MailException e) {
            log.error("Failed to send withdrawal email notification: " + e.getMessage());
        }

        return Result.success("withdraw success");
    }

    @Override
    public Result<Map<String, String>> getCardNumber(String token) throws Exception {
        if (token == null) {
            log.error("用户未登录，没有token.");
            throw new BusinessException(ResponseCode.USER_NOT_LOGIN.getCode(), "用户未登录，没有token.");
        }
        String username = JwtUtil.parseUserInfoFromToken(token);
        User user = userMapper.findByUsername(username);
        Card card = cardMapper.findByCardId(user.getCardId());
        Map<String, String> map = new HashMap<>();
        map.put("name", card.getName());
        map.put("cardNumber", card.getIban());
        return Result.success(map, "发送方姓名和卡号信息");
    }
}

