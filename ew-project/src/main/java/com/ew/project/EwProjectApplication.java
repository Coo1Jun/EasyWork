package com.ew.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"cn.edu.hzu", "com.ew.project"})
@EnableFeignClients(basePackages = {"cn.edu.hzu"})
@MapperScan("com.ew.project.**.**.mapper")
@EnableScheduling
public class EwProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwProjectApplication.class, args);
    }

}
