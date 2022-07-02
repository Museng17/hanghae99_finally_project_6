package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;

import static com.hanghae99.finalproject.config.WebConfig.SOCIAL_HEADER_KEY;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/login")
    public TokenResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        return userService.login(userRequestDto);
    }

    @PostMapping("/user/social")
    public TokenResponseDto socialLogin(@RequestHeader(SOCIAL_HEADER_KEY) String code) {
        return userService.findAccessTokenByCode(code);
    }

    @PostMapping("/user/refresh")
    public TokenResponseDto refreshToken(HttpServletRequest request) {
        return userService.createTokens(request.getAttribute("Authorization").toString());
    }

    @PostMapping("/user/signup")
    public UserRegisterRespDto registerUser(@RequestBody SignupDto Dto) throws NoSuchAlgorithmException {
        return userService.registerUser(Dto);
    }

    @ResponseBody
    @GetMapping("/user/emailDupCheck/{username}")
    public Boolean usernameDupCheck(@PathVariable String username) {

        return userService.checkUsernameDuplicate(username);
    }

    @ResponseBody
    @GetMapping("/user/nameDupCheck/{nickname}")
    public Boolean nameDupCheck(@PathVariable String nickname) {
        return userService.checkNameDuplicate(nickname);
    }

    @DeleteMapping("/user/getout/{id}")
    public Boolean userDelete(@PathVariable Long id, HttpServletRequest request) {

        return userService.UserDelete(id, request);
    }

}
