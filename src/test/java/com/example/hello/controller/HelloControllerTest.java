package com.example.hello.controller;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import redis.embedded.RedisServer;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest

class HelloControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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

    @Before
    public void setUp() throws Exception{
        doSetter();
        get();
    }

    @Test
    public void doSetter() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        String json = "{\"msg\":\"testers\"}";
//执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/hello")
                        .content(json.getBytes()) //传json参数
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andDo(print());
    }


    @Test
    public void get() throws Exception{
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/hello")
        );
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andDo(print());
    }

}

