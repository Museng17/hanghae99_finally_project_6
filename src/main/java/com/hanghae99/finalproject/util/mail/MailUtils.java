package com.hanghae99.finalproject.util.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.*;

@Slf4j
@Configuration
public class MailUtils {

    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    private final String PASSWORD;

    public MailUtils(TemplateEngine templateEngine,
                     @Value("${spring.mail.password}") String password) {
        this.templateEngine = templateEngine;
        this.PASSWORD = password;
    }

    @PostConstruct
    public void init() {
        javaMailSender = setJavaMailSender();
    }

    /*
     * 이메일 메세지와 제목 그리고 누구에게 보낼지 설정하는 메소드 (Text)
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
     * 이메일 메세지와 제목 그리고 누구에게 보낼지 설정하는 메소드 (HTML)
     * subject : 전송할 메세지의 제목
     * email : 보낼 이메일
     * htmlName : 보내줄 html name (/templates 하위에 넣어야함)
     * context : Thymeleaf 변수
     * */
    public MimeMessage makeMassageHtml(String subject, String email, String htmlName, Context context) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.addRecipients(MimeMessage.RecipientType.TO, email);
        mimeMessage.setSubject(subject);
        String name = templateEngine.process(htmlName, context);
        mimeMessage.setText(name, "utf-8", "html");
        return mimeMessage;
    }

    /*
     * text 이메일 전송하는 메소드
     * makeMassageText() 에서 만들어진 객체를 인자로 주면된다.
     * */
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        javaMailSender.send(simpleMailMessage);
    }

    /*
     * html 이메일 전송하는 메소드
     * makeMassageHtml() 에서 만들어진 객체를 인자로 주면된다.
     * */
    public void sendEmail(MimeMessage simpleMailMessage) {
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

    /*
     *  javaMailSender 설정
     * */
    public JavaMailSenderImpl setJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);

        javaMailSender.setUsername("moumcloud@gmail.com");
        javaMailSender.setPassword(PASSWORD);
        Properties prop = new Properties();

        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.debug", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.EnableSSL.enable", "true");
        javaMailSender.setJavaMailProperties(prop);
        return javaMailSender;
    }
}
