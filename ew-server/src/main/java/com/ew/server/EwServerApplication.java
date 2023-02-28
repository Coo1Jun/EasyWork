package com.ew.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"cn.edu.hzu", "com.ew.server"})
@MapperScan("com.ew.server.**.**.mapper")
public class EwServerApplication {

	public static void main(String[] args) {
        SpringApplication.run(EwServerApplication.class, args);
	}

}