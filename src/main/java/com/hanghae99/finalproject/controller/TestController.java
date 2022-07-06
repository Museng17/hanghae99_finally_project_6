package com.hanghae99.finalproject.controller;

import org.springframework.web.bind.annotation.*;

// github actions test용 controller
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        String testStr = "Hello World";
        System.out.println(testStr);
        return testStr;
    }
}
