package com.ew.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication(scanBasePackages = {"cn.edu.hzu", "com.ew.chat"})
@EnableFeignClients(basePackages = {"cn.edu.hzu"})
@MapperScan("com.ew.chat.**.**.mapper")
@EnableWebSocket
public class EwChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwChatApplication.class, args);
    }

}
