package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.exceptionHandler.CustumException.*;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.UserRepository;
import com.hanghae99.finalproject.model.resultType.LoginType;
import com.hanghae99.finalproject.singleton.CertificationMap;
import com.hanghae99.finalproject.util.mail.MailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.hanghae99.finalproject.exceptionHandler.CustumException.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private CertificationMap certificationMap = CertificationMap.getInstance();
    private final MailUtils mailUtils;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private MessageResponseDto sendEmailCertification(MailRequestDto mailRequestDto, String htmlFileName) {
        if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", mailRequestDto.getEmail())) {
            throw new CustomException(NOT_EMAIL);
        }
        String massage = mailUtils.makeRandomUUID(6);
        try {
            Context context = new Context();
            context.setVariable("massage", massage);
            mailUtils.sendEmail(mailUtils.makeMassageHtml("이메일 인증", mailRequestDto.getEmail(), htmlFileName, context));
        } catch (Exception e) {
            log.info("이메일 전송 실패");
            return new MessageResponseDto(500, "전송실패 : " + e.getMessage());
        }
        certificationMap.put(mailRequestDto.getEmail(), massage);
        return new MessageResponseDto(200, "전송완료");
    }

    public MessageResponseDto emailCheck(MailRequestDto mailRequestDto) {
        if(mailRequestDto.getCertification().trim().length() < 0) {
            throw new CustomException(NOT_CERTIFICATION);
        }
        if (!certificationCheck(mailRequestDto.getEmail(), mailRequestDto.getCertification())) {
            log.info("인증번호 요청 불일치");
            return new MessageResponseDto(404, "불일치");
        }
        certificationMap.put(mailRequestDto.getEmail(), true);
        return new MessageResponseDto(200, "일치");
    }

    public MessageResponseDto emailPasswordCheck(MailRequestDto mailRequestDto) {
        if(mailRequestDto.getCertification().trim().length() < 0) {
            throw new CustomException(NOT_CERTIFICATION);
        }
        if (!certificationCheck(mailRequestDto.getEmail(), mailRequestDto.getCertification())) {
            log.info("인증번호 요청 불일치");
            return new MessageResponseDto(404, "불일치");
        }
        certificationMap.put(mailRequestDto.getEmail(), true);
        return new MessageResponseDto(200, "일치");
    }

    public MessageResponseDto sendEmailAndUpdatePassword(MailRequestDto mailRequestDto) {
        if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", mailRequestDto.getEmail())) {
            throw new CustomException(NOT_EMAIL);
        }
        String str = getTempPassword();

        try {
            Context context = new Context();
            context.setVariable("massage", str);
            mailUtils.sendEmail(mailUtils.makeMassageHtml("임시 비밀번호 안내 이메일입니다.", mailRequestDto.getEmail(), "password", context));
        } catch (Exception e) {
            log.info(e.getMessage());
            return new MessageResponseDto(501, "전송실패 : " + e.getMessage());
        }

        log.info("임시 비밀번호 발급 완료");
        updatePassword(str, mailRequestDto.getEmail());
        return new MessageResponseDto(200, "전송완료");
    }

    public void updatePassword(String str, String email) {
        String password = bCryptPasswordEncoder.encode(str);
        Users user = userRepository.findUserByEmail(email);
        user.resetPw(password);
        userRepository.save(user);
    }

    public String getTempPassword() {
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                                      'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }

        return str;
    }

    public MessageResponseDto sendEmailForResetPassword(UserRequestDto userRequestDto) {
        MailRequestDto mailRequestDto = new MailRequestDto(userRequestDto);

        Users user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new CustomException(NOT_FIND_USER));

        if (user.getLoginType() == LoginType.GOOGLE || user.getLoginType() == LoginType.KAKAO) {

            return new MessageResponseDto(501, "소셜 로그인 유저");
        }

        if (user.getEmail().equals(userRequestDto.getEmail())) {
            sendEmailCertification(mailRequestDto, "mail");

            return new MessageResponseDto(200, "이메일 전송 완료");
        } else {

            return new MessageResponseDto(501, "회원 정보 불일치");
        }
    }

    @Transactional
    public MessageResponseDto sendRandomNewPassword(MailRequestDto mailRequestDto) {

        if (!certificationMap.match(mailRequestDto.getEmail())) {
            log.info("인증이 되지 않은 회원");
            return new MessageResponseDto(404, "인증을 하지 하지못한 회원입니다.");
        }
        sendEmailAndUpdatePassword(mailRequestDto);
        certificationMap.remove(mailRequestDto.getEmail(), true);

        return new MessageResponseDto(200, "임시 비밀번호 발급 성공");
    }

    @Transactional(readOnly = true)
    public MessageResponseDto sendEmail(MailRequestDto mailRequestDto) {
        Optional<Users> user = userRepository.findByEmail(mailRequestDto.getEmail());
        if (user.isPresent()) {
            throw new CustomException(OVERLAP_EMAIL);
        }
        return sendEmailCertification(mailRequestDto, "mail");
    }

    private boolean certificationCheck(String email, String certification) {
        return certificationMap.match(email, certification);
    }
}
