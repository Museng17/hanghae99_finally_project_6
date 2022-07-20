package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.MailRequestDto;
import com.hanghae99.finalproject.model.dto.requestDto.UserRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.MassageResponseDto;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.UserRepository;
import com.hanghae99.finalproject.service.MailService;
import com.hanghae99.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final UserRepository userRepository;

    @PostMapping("/email")
    public MassageResponseDto sendEmailCertification(@RequestBody MailRequestDto mailRequestDto) {
        return mailService.sendEmailCertification(mailRequestDto);
    }

    @PostMapping("/email/check")
    public MassageResponseDto emailCheck(@RequestBody MailRequestDto mailRequestDto) {
        return mailService.emailCheck(mailRequestDto);
    }

    @PostMapping("/email/sendResetPw")
    public MassageResponseDto sendEmailForResetPassword (@RequestBody UserRequestDto userRequestDto) {

        MailRequestDto mailRequestDto = new MailRequestDto(userRequestDto);

        Users user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("UserService 38에러 회원가입 되지 않은 이메일입니다."));

        if (user.getEmail().equals(userRequestDto.getEmail())) {
            mailService.sendEmailCertification(mailRequestDto);

            return new MassageResponseDto(200, "이메일 전송 완료");
        } else {

            return new MassageResponseDto(501, "회원 정보 불일치");
        }
    }
}
