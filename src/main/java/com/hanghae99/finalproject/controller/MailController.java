package com.hanghae99.finalproject.controller;

import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.MassageResponseDto;
import com.hanghae99.finalproject.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/email")
    public MassageResponseDto sendEmailCertification(@RequestBody MailRequestDto mailRequestDto) {
        return mailService.sendEmailCertification(mailRequestDto);
    }

    @PostMapping("/email/check")
    public MassageResponseDto emailCheck(@RequestBody MailRequestDto mailRequestDto) {
        return mailService.emailCheck(mailRequestDto);
    }

    @PostMapping("/email/sendResetPwCode")
    public MassageResponseDto sendEmailForResetPassword(@RequestBody UserRequestDto userRequestDto) {

        return mailService.sendEmailForResetPassword(userRequestDto);
    }

    @PostMapping("/email/sendNewPw")
    public MassageResponseDto sendRandomNewPassword(@RequestBody MailRequestDto mailRequestDto) {

        return mailService.sendRandomNewPassword(mailRequestDto);
    }
}
