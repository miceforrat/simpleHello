package com.example.hello.service;

import com.example.hello.data.HelloMessage;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HelloService {
    @Autowired
    private RedissonClient redisson;


    public HelloMessage getGreeting(HelloMessage msg){
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
