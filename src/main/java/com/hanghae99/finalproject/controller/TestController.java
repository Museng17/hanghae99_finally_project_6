package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// test용 controller
@RestController
@RequiredArgsConstructor
public class TestController {

    private final S3Uploader s3Uploader;
    @GetMapping("/test")
    public String test() {
        String testStr = "Hello World";
        System.out.println(testStr);
        return testStr;
    }

    @PostMapping("/images")
    public String upload(@RequestParam("images")MultipartFile multipartFile) throws IOException {
        s3Uploader.upload(multipartFile, "static");
        return "test";
    }
}
