package com.nus.team4.service;

import com.nus.team4.model.User;
import com.nus.team4.service.UserSvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserSvcTest {

    @Autowired
    private UserSvc userSvc;

    @Test
    public void testGetUserById() {
        // 假设数据库中有一个ID为1的用户
        Long userId = 1L;
        User user = userSvc.getUserById(userId);

        // 验证结果
        assertNotNull(user, "用户不应该为null");
    }
}
