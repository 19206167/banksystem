package com.nus.team4.service.impl;

import com.nus.team4.pojo.User;
import com.nus.team4.service.UserService;
import com.nus.team4.util.JwtUtil;
import com.nus.team4.vo.LoginUserInfo;
import com.nus.team4.vo.UsernameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nus.team4.mapper.UserMapper;

import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User getUserById(Long id) {
        log.info("调用方法： getUserById");
        return userMapper.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在或者密码错误。");
        }
        return user;
    }

    @Override
    public String login(UsernameAndPassword usernameAndPassword) throws Exception {
//        通过用户名和密码得到用户
        User user = userMapper.findByUsernameAndPassword(usernameAndPassword.getUsername(),
                usernameAndPassword.getPassword());
//        不能正常登录返回null
        if (user == null) {
            log.error("cannot find user: [{}]", usernameAndPassword.getUsername());
            return null;
        }

        LoginUserInfo loginUserInfo = new LoginUserInfo(user.getId(), user.getUsername());

        return JwtUtil.createJWT(loginUserInfo);
    }

    @Override
    public String register(UsernameAndPassword usernameAndPassword) {
        return null;
    }
}

