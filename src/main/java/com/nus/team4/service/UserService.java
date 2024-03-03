package com.nus.team4.service;

import com.nus.team4.advice.Result;
import com.nus.team4.dto.request.AccountOpenForm;
import com.nus.team4.exception.BusinessException;
import com.nus.team4.pojo.User;
import com.nus.team4.vo.RegistrationForm;


public interface UserService {
    User getUserById(Long id);

//    Result<JwtToken> login(UsernameAndPassword usernameAndPassword) throws Exception;

    Result<String> getIban(String token) throws Exception;

    Result<String> getName(String token) throws Exception;

    Result logout(String token) throws Exception;

    Result register(RegistrationForm registrationForm) throws BusinessException;

    Result openAccount(AccountOpenForm accountOpenForm);
}
