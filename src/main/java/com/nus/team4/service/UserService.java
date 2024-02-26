package com.nus.team4.service;


import com.nus.team4.pojo.User;
import com.nus.team4.vo.UsernameAndPassword;

public interface UserService {
    User getUserById(Long id);

    User getUserByUsername(String username);

    String login(UsernameAndPassword usernameAndPassword) throws Exception;

    String register(UsernameAndPassword usernameAndPassword);
}
