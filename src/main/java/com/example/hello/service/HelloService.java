package com.example.hello.service;

import com.example.hello.data.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {
    private String message;
    public HelloMessage getGreeting(HelloMessage msg){
        message = msg.getMsg();
        return msg;
    }

    public String getMsgShown(){
        return message;
    }
}
