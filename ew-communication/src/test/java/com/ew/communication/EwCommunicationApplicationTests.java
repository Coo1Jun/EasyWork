package com.ew.communication;

import cn.edu.hzu.common.api.utils.DateUtils;
import com.ew.communication.message.entity.Message;
import com.ew.communication.message.service.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EwCommunicationApplicationTests {

    @Autowired
    private IMessageService messageService;

    @Test
    void contextLoads() {
        System.out.println(DateUtils.parseDate(DateUtils.getDate()));
    }

}
