package com.nus.team4.service;


import com.nus.team4.advice.Result;
import com.nus.team4.pojo.User;
import com.nus.team4.dto.AccountOpenForm;
import com.nus.team4.vo.JwtToken;
import com.nus.team4.vo.RegistrationForm;
import com.nus.team4.vo.UsernameAndPassword;

public interface UserService {
    User getUserById(Long id);

    User getUserByUsername(String username);

    Result<JwtToken> login(UsernameAndPassword usernameAndPassword) throws Exception;

    Result register(RegistrationForm registrationForm);

    Result openAccount(AccountOpenForm accountOpenForm);
}
