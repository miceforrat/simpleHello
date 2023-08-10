package com.example.hello;

import com.example.hello.data.HelloMessage;
import com.example.hello.service.HelloService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HelloServiceTest {
    @Resource
    private HelloService helloService;

    @Test
    public void testMsgSetterGetter(){
        HelloMessage message = new HelloMessage();
        message.msg = "tester";
        Assertions.assertEquals(message.msg, helloService.getGreeting(message).msg);

        Assertions.assertEquals(message.msg, helloService.getMsgShown());
    }

}