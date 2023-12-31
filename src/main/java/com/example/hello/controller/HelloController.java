package com.example.hello.controller;

import com.example.hello.data.HelloMessage;
import com.example.hello.limits.MyRedisRLimiter;
import com.example.hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;


//    private RateLimiter limiter = RateLimiter.create(100);

    @PostMapping("/hello")
    @MyRedisRLimiter(name = "greet", period = 1, count = 10, key = "greet_key")
    public ResponseEntity<Object> responseToGreeting(@RequestBody HelloMessage msgIn){

        return ResponseEntity.status(200).body(helloService.getGreeting(msgIn));
    }


    @GetMapping("/hello")
    @MyRedisRLimiter(name = "greet", period = 1, count = 100, key = "get_greet_key")
    public String showMsg(){

        return helloService.getMsgShown();
    }

}
