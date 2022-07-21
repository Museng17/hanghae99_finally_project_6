package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.responseDto.MassageResponseDto;
import com.hanghae99.finalproject.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    public MassageResponseDto imageUpload(@RequestParam("image") MultipartFile multipartFile) {
        return imageService.imageUpload(multipartFile);
    }
}
