package com.nus.team4.service.impl;

import com.nus.team4.advice.Result;
import com.nus.team4.common.ResponseCode;
import com.nus.team4.dto.request.AccountOpenForm;
import com.nus.team4.dto.response.CardInfo;
import com.nus.team4.constant.AuthorityConstant;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.mapper.CardMapper;
import com.nus.team4.pojo.Card;
import com.nus.team4.pojo.User;
import com.nus.team4.service.UserService;
import com.nus.team4.util.AccountUtil;
import com.nus.team4.util.EncryptionUtil;
import com.nus.team4.util.JwtUtil;
import com.nus.team4.util.RedisUtil;
import com.nus.team4.vo.RegistrationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nus.team4.mapper.UserMapper;


import java.math.BigDecimal;

import java.util.Date;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    private CardMapper cardMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, CardMapper cardMapper) {
        this.userMapper = userMapper;
        this.cardMapper = cardMapper;
    }

    public User getUserById(Long id) {
        log.info("调用方法： getUserById");
        return userMapper.findById(id);
    }

//    @Override
//    public User getUserByUsername(String username) {
//        User user = userMapper.findByUsername(username);
//        if (Objects.isNull(user)) {
//            throw new RuntimeException("用户不存在或者密码错误。");
//        }
//        return user;
//    }

//    @Override
//    public Result<JwtToken> login(UsernameAndPassword usernameAndPassword) throws Exception {
////        通过用户名和密码得到用户
//        User user = userMapper.findByUsernameAndPassword(usernameAndPassword.getUsername(),
//                usernameAndPassword.getPassword());
//
////        不能正常登录返回null
//        if (user == null) {
//            log.error("cannot find user: [{}]", usernameAndPassword.getUsername());
//            return Result.error(0, "cannot find user");
//        }
//
//        LoginUserInfo loginUserInfo = new LoginUserInfo(user.getId(), user.getUsername());
//
//        String jwt = JwtUtil.createJWT(loginUserInfo);
//
////        将jwt存入redis中，鉴权时取出
//        redisUtil.set(AuthorityConstant.JWT_USER_INFO_KEY +usernameAndPassword.getUsername(), jwt);
//
//        return Result.success(new JwtToken(jwt), "登录成功");
//    }

//    注册账户方法
    /*
     * @description:
     * 首先判断username是否已占用
     * 然后从数据库获取卡号，如果没有返回错误
     * 然后判断卡号是否已注册，每个卡号只能注册一次
     * 如果都满足，进行注册，添加一条新数据
     **/
    @Override
    public Result<String> register(RegistrationForm registrationForm) throws BusinessException {
        log.info(registrationForm.getUsername());
        User user = userMapper.findByUsername(registrationForm.getUsername());
        if (user != null) {
            log.error("username has been registered.");
            throw new BusinessException(ResponseCode.USER_ACCOUNT_ALREADY_EXIST.getCode(), "username has been registered.");
        }

        Card card = cardMapper.findByCardNumber(registrationForm.getCardNumber());

        if (card == null) {
            log.info("card number no exists. ");
            throw new BusinessException(ResponseCode.USER_CARD_NOT_EXIST.getCode(), "card number not exists");
        }
        if (!card.getSecurityCode().equals(registrationForm.getSecurityCode())) {
            log.info("card security wrong!");
            log.info("request card cvc:");
            log.info(registrationForm.getSecurityCode());
            log.info(card.getSecurityCode());
            throw new BusinessException(ResponseCode.CARD_SECURITY_WRONG.getCode(), "card security wrong!");
        }

        user = userMapper.findByCardId(card.getId());

        if (user != null) {
            log.info("card has been bound to another account.");
            throw new BusinessException(ResponseCode.USER_ACCOUNT_DISABLE.getCode(), "card has been bound to another account.");
        }

        userMapper.createNewUser(card.getId(),
                registrationForm.getUsername(), encryptionUtil.encrypt(registrationForm.getPassword()));

        return Result.success("registration success!");
    }

    @Override
    public Result<String> getIban(String token) throws Exception {
        String username = JwtUtil.parseUserInfoFromToken(token);
        User user = userMapper.findByUsername(username);
        Card card = cardMapper.findByCardID(user.getCardId());

        return Result.success(card.getIban());
    }
    @Override
    public Result<String> logout(String token) throws Exception {
        String username = JwtUtil.parseUserInfoFromToken(token);
//        让redis中的token失效
        StringBuilder key = new StringBuilder(AuthorityConstant.JWT_USER_INFO_KEY).append(username);
        redisUtil.expire(key.toString(), 0L, TimeUnit.SECONDS);
//        redisUtil.set(AuthorityConstant.TOKEN_BLACKLIST_CACHE_PREFIX + token, token, 11L, TimeUnit.MINUTES);

        return Result.success("已成功登出");
    }

    @Override
    public Result openAccount(AccountOpenForm accountOpenForm){
        String iban = AccountUtil.generateAccountNumber();
        String cvc = AccountUtil.generateCVC();
        Card account = Card.builder()
                .iban(iban)
                .email(encryptionUtil.encrypt(accountOpenForm.getEmail()))
                .name(accountOpenForm.getName())
                .phone(encryptionUtil.encrypt(accountOpenForm.getPhone()))
                .SecurityCode(cvc)
                .currency(accountOpenForm.getCurrency())
                .accountType(accountOpenForm.getAccountType())
                .status("Active")
                .balance(new BigDecimal("0.00"))
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        cardMapper.insertCard(account);
        CardInfo cardInfo = new CardInfo(iban, cvc);
        return Result.success(cardInfo, "account created");
    }
}