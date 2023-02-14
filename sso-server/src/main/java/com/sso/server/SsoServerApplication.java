package com.sso.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"cn.edu.hzu", "com.sso.server"})
@MapperScan("com.sso.server.**.**.mapper")
public class SsoServerApplication {

	public static void main(String[] args) {
        SpringApplication.run(SsoServerApplication.class, args);
	}

}