package com.nus.team4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

// test github workflow auto test
@RunWith(SpringRunner.class)
@SpringBootTest
public class EnvironmentTest {

    @Test
    public void run(){
        System.out.println(1/0);
    }

}
