package com.ew.server;

import cn.edu.hzu.common.service.MailService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

/**
 *
 */
//@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	ApplicationContext ctx;

	@Autowired
	MailService mailService;

	@Test
	public void testContextLoads() throws Exception {
		Assert.assertNotNull(this.ctx);
		Assert.assertTrue(this.ctx.containsBean("application"));
	}

	@Test
	public void testSendEmail() throws MessagingException {
//		mailService.sendMail("1425176577@qq.com", "Test Email", "Hello, this is a test email!");
		Context context = new Context();
		context.setVariable("title", "Test Email");
		context.setVariable("content", "Hello, this is a test email!");
		mailService.sendMail("1425176577@qq.com", "Test Email", "email-templates", context);
	}

}