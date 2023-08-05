package com.example.hello.controller;

import com.example.hello.data.HelloMessage;
import com.example.hello.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;

    @PostMapping("/hello")
    public HelloMessage responseToGreeting(@RequestBody HelloMessage msgIn){
        return helloService.getGreeting(msgIn);
    }


    @GetMapping("/hello")
    public String showMsg(){
        return helloService.getMsgShown();
    }

}
