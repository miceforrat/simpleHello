package com.example.hello.service;

import com.example.hello.HelloApplication;
import com.example.hello.data.HelloMessage;
import jakarta.annotation.Resource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;
//import redis.embedded.RedisServer;


@SpringBootTest
@RunWith(SpringRunner.class)
//@ActiveProfiles("unittest")
//@ContextConfiguration(locations= {"classpath*:application-test.yml"})
class HelloServiceTest {
    @Resource
    private HelloService helloService;

    private static RedisServer redisServer;

    /**
     * 启动Redis，并在6379端口监听
     */
    @BeforeAll
    public static void startRedis() {
        // https://github.com/kstyrc/embedded-redis/issues/51
        redisServer = RedisServer.builder()
                .port(6379)
                .setting("maxmemory 128M") //maxheap 128M
                .build();

        redisServer.start();

    }

    /**
     * 析构方法之后执行，停止Redis.
     */
    @AfterAll
    public static void stopRedis() {
        redisServer.stop();
    }


    @Test
    public void testMsgSetterGetter(){
        HelloMessage message = new HelloMessage();
        message.msg = "tester";
        Assertions.assertEquals(message.msg, helloService.getGreeting(message).msg);

        Assertions.assertEquals(message.msg, helloService.getMsgShown());
    }

}