package com.ew.server.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author lzf
 * @create 2023/03/01
 * @description 邮件发送
 */
@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 简单邮件发送
     * @param to 邮件的接收方
     * @param subject 标题
     * @param text 内容
     */
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(username);
        javaMailSender.send(message);
    }
}
