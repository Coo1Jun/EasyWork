package com.ew.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.hzu", "com.ew.project"})
@MapperScan("com.ew.project.**.**.mapper")
public class EwProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwProjectApplication.class, args);
    }

}
