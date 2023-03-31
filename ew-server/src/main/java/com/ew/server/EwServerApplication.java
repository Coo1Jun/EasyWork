package com.ew.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"cn.edu.hzu", "com.ew.server"})
@EnableFeignClients(basePackages = {"cn.edu.hzu"})
@MapperScan("com.ew.server.**.**.mapper")
public class EwServerApplication {

	public static void main(String[] args) {
        SpringApplication.run(EwServerApplication.class, args);
	}

}