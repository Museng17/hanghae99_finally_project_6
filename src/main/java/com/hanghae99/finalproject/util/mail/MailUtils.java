package com.hanghae99.finalproject.util.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class MailUtils {

    private final JavaMailSender javaMailSender;

    /*
     * 이메일 메세지와 제목 그리고 누구에게 보낼지 설정하는 메소드
     * massage : 전송할 메세지
     * subject : 전송할 메세지의 제목
     * toUser : 보낼 이메일
     * */
    public SimpleMailMessage makeMassageText(String massage, String subject, String toUser) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(toUser);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(massage);
        return simpleMailMessage;
    }

    /*
     * 이메일 전송하는 메소드
     * */
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        javaMailSender.send(simpleMailMessage);
    }

    /*
     * 랜덤값의 값이 설정되는 메소드
     * size : 랜덤값의 사이즈를 입력받음
     * ex ) makeRandomUUID(4) =  1515  ,  makeRandomUUID(7) = 1515157
     * */
    public String makeRandomUUID(int size) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, size);
    }
}
