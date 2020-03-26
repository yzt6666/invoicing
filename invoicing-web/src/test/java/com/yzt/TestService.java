package com.yzt;

import com.yzt.service.StockService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Test
    public void Test() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("你好呀");
        mailMessage.setText("我很好");
        mailMessage.setFrom("565872132@qq.com");
        mailMessage.setTo("565872132@qq.com");
        mailSender.send(mailMessage);

    }

}
