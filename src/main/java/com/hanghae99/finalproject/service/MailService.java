package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.requestDto.MailRequestDto;
import com.hanghae99.finalproject.model.dto.responseDto.MassageResponseDto;
import com.hanghae99.finalproject.singleton.CertificationMap;
import com.hanghae99.finalproject.util.mail.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.ListIterator;

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
            mailUtils.sendEmail(mailUtils.makeMassageHtml("이메일 인증", mailRequestDto.getEmail(), "mail", context));
        } catch (Exception e) {
            log.info("이메일 전송 실패");
            return new MassageResponseDto(501, "전송실패 : " + e.getMessage());
        }
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

    @Scheduled(cron = "00 08 20 * * *")
    private void singletonRemove2() {
        log.info("스케쥴러 작동");
        log.info("삭제되기전 인증요청 카운트 : " + certificationMap.getCertificationMapSize());
        int checkCnt = 0;
        try {
            ListIterator<String> listIterator = certificationMap.getUsedList().listIterator();
            for (ListIterator<String> iterator = listIterator; iterator.hasNext(); ) {
                String key = iterator.next();
                if (certificationMap.isTimeOver(key)) {
                    certificationMap.remove(key);
                    iterator.remove();
                    checkCnt++;
                }
            }
            log.info("스케쥴러 작동 완료 삭제된 데이터 카운트 : " + checkCnt);
            throw new RuntimeException();
        } catch (Exception e) {
            log.info("스케쥴러 작동 실패 에러 메세지 : ");
            log.info(e.toString());
        }

    }
}
