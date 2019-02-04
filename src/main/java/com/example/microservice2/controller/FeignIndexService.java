package com.example.microservice2.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "index")
public interface FeignIndexService {

    @GetMapping("/prop")
    String getMS2Prop();

    @PostMapping("/index")
    void indexContent(@RequestParam("bytes") byte[] bytes, @RequestParam("id") long id);
}
