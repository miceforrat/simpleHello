package com.example.hello.controller;

import com.example.hello.service.PrometheusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusController {


    private final PrometheusService prometheusService;

    @Autowired
    public PrometheusController(PrometheusService prometheusService){
        this.prometheusService=prometheusService;
    }

    @GetMapping("/metric")
    public String handleRequest(){
        prometheusService.processRequest();
        return "Request processed!";
    }

}
