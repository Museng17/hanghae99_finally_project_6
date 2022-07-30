package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.service.*;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static com.hanghae99.finalproject.config.WebConfig.SOCIAL_HEADER_KEY;
import static com.hanghae99.finalproject.jwt.JwtTokenProvider.REFRESH_TOKEN;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Uploader s3Uploader;
    private final UserinfoHttpRequest userinfoHttpRequest;

    private final FollowService followService;

    @PostMapping("/user/login")
    public TokenResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        return userService.login(userRequestDto);
    }

    @PostMapping("/user/social")
    public TokenResponseDto socialLogin(@RequestHeader(SOCIAL_HEADER_KEY) String code) {
        return userService.findAccessTokenByCode(code);
    }

    @PostMapping("/user/social2")
    public TokenResponseDto socialLogin2(@RequestHeader(SOCIAL_HEADER_KEY) String code) {
        return userService.findAccessTokenByCode2(code);
    }

    @PostMapping("/user/refresh2")
    public TokenResponseDto refreshToken2(HttpServletRequest request,
                                          @RequestHeader(value = REFRESH_TOKEN, defaultValue = "noToken") String refresh) {
        return userService.refreshToken2(refresh);
    }

    @PostMapping("/user/refresh")
    public TokenResponseDto refreshToken(HttpServletRequest request,
                                         @RequestHeader(value = REFRESH_TOKEN, defaultValue = "noToken") String refresh) {
        return userService.refreshToken(refresh);
    }

    @PostMapping("/user/signup")
    public UserRegisterRespDto registerUser(@RequestBody UserRequestDto Dto) {
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

    @PostMapping("/find/username")
    public String findUsername(@RequestBody UserRequestDto userRequestDto) {

        return userService.findUsername(userRequestDto);
    }

    @DeleteMapping("/user/getout")
    public MessageResponseDto userDelete(HttpServletRequest request) {

        return userService.UserDelete(request);
    }

    @PutMapping("/user/updateName")
    public MessageResponseDto userUpdate(@RequestBody UserRequestDto userRequestDto,
                              HttpServletRequest request) {

        return userService.updateUserNickname(userRequestDto, request);
    }

    @PutMapping("/user/updateInfo")
    public MessageResponseDto userUpdateInfo(@RequestBody UserRequestDto userRequestDto,
                                  HttpServletRequest request) {

        return userService.updateUserInfo(userRequestDto, request);
    }

    @PutMapping("/user/pw/update")
    public MessageResponseDto userPwUpdate(@RequestBody UserRequestDto userRequestDto,
                                HttpServletRequest request) {

        return userService.updateUserPw(userRequestDto, request);
    }

    @PostMapping("/user/pw/check")
    public MessageResponseDto userPwCheck(@RequestBody UserRequestDto userRequestDto,
                               HttpServletRequest request) {

        return userService.checkUserPw(userRequestDto, request);
    }

    @GetMapping("/user/profile")
    public Users findUserProfile(HttpServletRequest request) {

        return userService.findUserProfile(request);
    }

    @GetMapping("/user/profile/{id}")
    public UserProfileDto findUserProfile(Model model, @PathVariable Long id, HttpServletRequest request) {
        model.addAttribute("userProfileDto");

        return userService.getProfile(id, request);
    }

    @GetMapping("/user/myProfile")
    public MyProfileDto findMyProfile(Model model, HttpServletRequest request) {
        model.addAttribute("myProfileDto");

        return userService.getMyProfile(request);
    }

    @PostMapping("/user/profilePhoto")
    public ResponseEntity<?> uploadProfilePhoto(HttpServletRequest request,
                                                @RequestParam("profilePhoto") MultipartFile multipartFile) throws IOException {

        Users user = userinfoHttpRequest.userFindByToken(request);
        FileUploadResponse profile = s3Uploader.upload(user.getId(), multipartFile, "profile");
        userService.updateUserImg(profile.getUrl(), request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/followinguser/{page}/{size}")
    public MessageResponseDto findFollowingUser(@PathVariable int page, @PathVariable int size, HttpServletRequest request) {
        return followService.findFollowingUser(page, size, request);
    }
}
