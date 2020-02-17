package com.yzt;

import com.yzt.entity.LoginUser;
import com.yzt.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {
    @Autowired
    private UserService userService;

    @Test
    public void Test() {
        LoginUser user = userService.selUser("张三");
        System.out.println(user.getPassword());
    }

}
