package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.service.FollowService;
import com.hanghae99.finalproject.service.S3Uploader;
import com.hanghae99.finalproject.service.UserService;
import com.hanghae99.finalproject.util.UserinfoHttpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static com.hanghae99.finalproject.config.WebConfig.SOCIAL_HEADER_KEY;
import static com.hanghae99.finalproject.jwt.JwtTokenProvider.REFRESH_TOKEN;

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

    @PostMapping("/user/refresh")
    public TokenResponseDto refreshToken(@RequestHeader(REFRESH_TOKEN) String refresh) {
        return userService.refreshToken(refresh);
    }

    @PostMapping("/user/signup")
    public UserRegisterRespDto registerUser(@RequestBody UserRequestDto Dto) throws NoSuchAlgorithmException {
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

    @PutMapping("/user/updateName/{id}")
    public Boolean userUpdate(@PathVariable Long id,
                              @RequestBody UserRequestDto userRequestDto,
                              HttpServletRequest request) {

        return userService.updateUserName(id, userRequestDto, request);
    }

    @PutMapping("/user/updateInfo/{id}")
    public Boolean userUpdateInfo(@PathVariable Long id,
                                  @RequestBody UserRequestDto userRequestDto,
                                  HttpServletRequest request) {

        return userService.updateUserInfo(id, userRequestDto, request);
    }

    @PutMapping("/user/pw/update/{id}")
    public Boolean userPwUpdate(@PathVariable Long id,
                                @RequestBody UserRequestDto userRequestDto,
                                HttpServletRequest request) {

        return userService.updateUserPw(id, userRequestDto, request);
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

    @PostMapping("/user/profilePhoto/{id}")
    public ResponseEntity<?> uploadProfilePhoto(@PathVariable Long id,
                                                HttpServletRequest request,
                                                @RequestParam("profilePhoto") MultipartFile multipartFile) throws IOException {

        Users user = userinfoHttpRequest.userFindByToken(request);
        FileUploadResponse profile = s3Uploader.upload(user.getId(), multipartFile, "profile");
        userService.updateUserImg(id, profile.getUrl(), request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/followinguser/{page}/{size}")
    public List<UserRequestDto> findFollowingUser(@PathVariable int page, @PathVariable int size, HttpServletRequest request){
        return followService.findFollowingUser(page,size,request);
    }
}
