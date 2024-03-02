package com.nus.team4.service.impl;

import com.nus.team4.mapper.UserMapper;
import com.nus.team4.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("调用loadUserByUsername方法");

        com.nus.team4.pojo.User user = userMapper.findByUsername(username);

        // 用户不存在，抛出异常
        if (user == null) {
            log.error("用户名不存在：[{}]", username);
            throw new UsernameNotFoundException("用户名不存在！");
        }

        // 不涉及具体权限，随便整一个，不能为空
        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList("role");

        return new User(user.getUsername(),
                new BCryptPasswordEncoder().encode(encryptionUtil.decrypt(user.getPassword())), authorities);
    }
}
