package cn.edu.hzu.common.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * @author lzf
 * @create 2023/03/01
 * @description 邮件发送
 */
@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String username;

    /**
     * 简单邮件发送
     * @param to 邮件的接收方
     * @param subject 标题
     * @param text 内容
     */
    public void sendMail(String to, String subject, String text) {
        this.sendMail(new String[]{to}, subject, text);
    }

    /**
     * 简单邮件发送
     * @param to 邮件的接收方
     * @param subject 标题
     * @param text 内容
     */
    public void sendMail(String[] to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(username);
        javaMailSender.send(message);
    }

    /**
     * 模板邮件发送
     * @param to 邮件的接收方
     * @param subject 标题
     * @param templateName 模板名字（在src/main/resources/templates目录下）
     * @param context 模板中需要替换的变量由Context封装 context.setVariable(key, value)
     * @throws MessagingException
     */
    public void sendMail(String to, String subject, String templateName, Context context) throws MessagingException {
        this.sendMail(new String[]{to}, subject, templateName, context);
    }

    /**
     * 模板邮件发送
     * @param to 邮件的接收方
     * @param subject 标题
     * @param templateName 模板名字（在src/main/resources/templates目录下）
     * @param context 模板中需要替换的变量由Context封装 context.setVariable(key, value)
     * @throws MessagingException
     */
    public void sendMail(String[] to, String subject, String templateName, Context context) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(username);
        String htmlContent = templateEngine.process(templateName, context);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    /**
     * 模板邮件发送
     * @param to 邮件的接收方
     * @param subject 标题
     * @param templateName 模板名字（在src/main/resources/templates目录下）
     * @param context 模板中需要替换的变量由Context封装 context.setVariable(key, value)
     * @throws MessagingException
     */
    public void sendMail(List<String> to, String subject, String templateName, Context context) throws MessagingException {
        this.sendMail((String[]) to.toArray(), subject, templateName, context);
    }

}
