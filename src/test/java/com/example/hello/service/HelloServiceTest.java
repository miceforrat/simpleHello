package com.example.hello.service;

import com.example.hello.HelloApplication;
import com.example.hello.data.HelloMessage;
import com.example.hello.service.HelloService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;


@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootTest(classes = HelloApplication.class)
@RunWith(SpringRunner.class)
class HelloServiceTest {
    @Resource
    private HelloService helloService;

//    private static RedisServer redisServer;
//
//    /**
//     * 启动Redis，并在6379端口监听
//     */
//    @BeforeAll
//    static void startRedis() {
//        // https://github.com/kstyrc/embedded-redis/issues/51
//        redisServer = RedisServer.builder()
//                .port(6379)
//                .setting("maxmemory 128M") //maxheap 128M
//                .build();
//
//        redisServer.start();
//
//    }
//
//    /**
//     * 析构方法之后执行，停止Redis.
//     */
//    @AfterAll
//    static void stopRedis() {
//        redisServer.stop();
//    }


    @Test
    public void testMsgSetterGetter(){
        HelloMessage message = new HelloMessage();
        message.msg = "tester";
        Assertions.assertEquals(message.msg, helloService.getGreeting(message).msg);

        Assertions.assertEquals(message.msg, helloService.getMsgShown());
    }

}