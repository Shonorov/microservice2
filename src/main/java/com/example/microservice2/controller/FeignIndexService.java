package com.example.microservice2.controller;

import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
@FeignClient(name = "index")
public interface FeignIndexService {

    @GetMapping("/prop")
    String getMS2Prop();

    @PostMapping("/index")
    void indexContent(@RequestParam("bytes") byte[] bytes, @RequestParam("id") long id);

    @PostMapping(value = "/indexFile", consumes = {"multipart/form-data"})
    @Headers("Content-Type: multipart/form-data")
    void indexFileContent(@Param("file") MultipartFile file, @RequestParam("id") long id);
}
