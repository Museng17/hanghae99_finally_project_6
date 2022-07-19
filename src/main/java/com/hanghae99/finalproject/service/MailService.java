package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.MailRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.MassageResponseDto;
import com.hanghae99.finalproject.singleton.CertificationMap;
import com.hanghae99.finalproject.util.mail.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private CertificationMap certificationMap = CertificationMap.getInstance();

    private final MailUtils mailUtils;

    public MassageResponseDto sendEmailCertification(MailRequestDto mailRequestDto) {
        String massage = mailUtils.makeRandomUUID(6);

        try {
            Context context = new Context();
            context.setVariable("massage", massage);

            mailUtils.sendEmail(mailUtils.makeMassageHtml("인증번호 : " + massage, "이메일 인증", mailRequestDto.getEmail(), context));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new MassageResponseDto(501, "전송실패 : " + e.getMessage());
        }

        log.info("이메일 전송 완료");
        certificationMap.put(mailRequestDto.getEmail(), massage);
        return new MassageResponseDto(200, "전송완료");
    }

    public MassageResponseDto emailCheck(MailRequestDto mailRequestDto) {
        if (!certificationMap.match(mailRequestDto.getEmail(), mailRequestDto.getCertification())) {
            log.info("인증번호 요청 불일치");
            return new MassageResponseDto(404, "불일치");
        }
        certificationMap.remove(mailRequestDto.getEmail());
        return new MassageResponseDto(200, "일치");
    }
}
