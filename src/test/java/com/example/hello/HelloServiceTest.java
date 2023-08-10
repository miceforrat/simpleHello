package com.example.hello;

import com.example.hello.data.HelloMessage;
import com.example.hello.service.HelloService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
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