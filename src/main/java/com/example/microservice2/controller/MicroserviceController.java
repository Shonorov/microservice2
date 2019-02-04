package com.example.microservice2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicroserviceController {

    @Autowired
    private FeignIndexService feignMS1;

    @Value("${test.value}")
    private String property;;

    @GetMapping(value = "/prop")
    public String getProperty() {
        return property;
    }

    @GetMapping(value = "/prop1")
    public String getMS1Property() {
        return "Value from microservice1: " + feignMS1.getMS2Prop();
    }
}
