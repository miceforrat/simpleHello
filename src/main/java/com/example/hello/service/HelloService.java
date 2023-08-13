package com.example.hello.service;

import com.example.hello.data.HelloMessage;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HelloService {
    private String message;

    @Autowired
    private RedissonClient redisson;

//    HelloService(){
//        Map<String, String> cacheMap = redisson.getMap("cacheInfo");
//        cacheMap.putIfAbsent("getMsg", "");
//    }

    public HelloMessage getGreeting(HelloMessage msg){
//        message = msg.msg;
        Map<String, String> cacheMap = redisson.getMap("cacheInfo");
        if (!cacheMap.containsKey("getMsg")){
            cacheMap.put("getMsg", msg.msg);
        } else {
            cacheMap.replace("getMsg", msg.msg);
        }

        return msg;
    }

    public String getMsgShown(){
        Map<String, String> cacheMap = redisson.getMap("cacheInfo");
        if (!cacheMap.containsKey("getMsg")){
            cacheMap.put("getMsg", "");
            return "";
        }
        return cacheMap.get("getMsg");
    }
}
