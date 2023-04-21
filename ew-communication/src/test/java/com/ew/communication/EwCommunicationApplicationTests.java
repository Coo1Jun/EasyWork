package com.ew.communication;

import com.ew.communication.message.entity.Message;
import com.ew.communication.message.service.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EwCommunicationApplicationTests {

    @Autowired
    private IMessageService messageService;

    @Test
    void contextLoads() {
        Message message = new Message();
        Message message1 = new Message();
        Map<String, Message> map = new ConcurrentHashMap<>();
        map.put("1", message);
        map.put("2", message1);
        System.out.println(map);
        map.remove("2");
        System.out.println(map);

    }

}
