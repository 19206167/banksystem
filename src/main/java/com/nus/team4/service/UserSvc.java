package com.nus.team4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nus.team4.mapper.UserMapper;
import com.nus.team4.model.User;

@Service
public class UserSvc {
    private final UserMapper userMapper;

    @Autowired
    public UserSvc(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Long id) {
        return userMapper.findById(id);
    }
}

