package com.hanghae99.finalproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// test용 controller
@RestController
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/test")
    public String test() {
        String testStr = "Hello World";
        System.out.println(testStr);
        return testStr;
    }

    @PostMapping("/test/test")
    public void tes2t(@RequestParam("test") MultipartFile multipartFile) {
    }
}
