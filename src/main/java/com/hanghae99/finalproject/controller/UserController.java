package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/login")
    public TokenResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        return userService.login(userRequestDto);
    }

    @PostMapping("/user/refresh")
    public TokenResponseDto refreshToken(HttpServletRequest request) {
        return userService.createTokens(request.getAttribute("Authorization").toString());
    }
}
