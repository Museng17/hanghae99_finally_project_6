package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/login")
    public TokenResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        return userService.login(userRequestDto);
    }

    @PostMapping("/user/social")
    public TokenResponseDto socialLogin(@RequestHeader("Credential") String credential) {
        return userService.socialLogin(credential);
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

    @PutMapping("/user/update/{id}")
    public Boolean userUpdate(@PathVariable Long id,
                              @RequestBody UserRequestDto userRequestDto,
                              HttpServletRequest request) {
        return userService.updateUserInfo(id, userRequestDto, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMassageResponseDto exceptionHandler(Exception e) {
        return new ErrorMassageResponseDto(e.getMessage());
    }

}
