package com.example.hello.testTool;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import redis.embedded.RedisServer;

public class RedisServerMocker {
    private static RedisServer redisServer;

    /**
     * 启动Redis，并在6379端口监听
     */
    @BeforeAll
    public static void startRedis() {
        System.setProperty("cur_stage", "test");
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
}
