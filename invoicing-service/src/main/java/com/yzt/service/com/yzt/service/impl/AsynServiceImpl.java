package com.yzt.service.com.yzt.service.impl;

import com.yzt.service.AsynService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AsynServiceImpl implements AsynService {
    @Autowired
    private JavaMailSenderImpl mailSender;
    /*
        定时发送邮件
        cron: 秒 分 时 日 月 周几
     */
    @Scheduled(cron = "0 0 0 * * 0-7")
    @Override
    public void sendMail() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("你好呀");
        mailMessage.setText("我很好");
        mailMessage.setFrom("565872132@qq.com");
        mailMessage.setTo("565872132@qq.com");
        mailSender.send(mailMessage);
    }
}
