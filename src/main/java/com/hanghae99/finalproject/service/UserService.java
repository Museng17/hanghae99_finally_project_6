package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.jwt.JwtTokenProvider;
import com.hanghae99.finalproject.model.dto.*;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.UserRepository;
import com.hanghae99.finalproject.util.restTemplates.SocialLoginRestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginRestTemplate socialLoginRestTemplate;

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserRequestDto userRequestDto) {
        Users user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("회원가입되지 않은 아이디입니다."));

        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        return createTokens(user.getUsername());
    }

    public TokenResponseDto createTokens(String username) {
        return new TokenResponseDto(
                jwtTokenProvider.createAccessToken(username),
                jwtTokenProvider.createRefreshToken(username)
        );
    }

    public Boolean checkUsernameDuplicate(String username) {
        Users user = userRepository.findByUsername(username).orElse(null);

        try {
            if (user.getUsername().equals(username)) {
                return false;
            }
        } catch (NullPointerException e) {
            return true;
        }
        return true;
    }

    public Boolean checkNameDuplicate(String nickname) {
        Users user = userRepository.findByNickname(nickname).orElse(null);

        try {
            if (user.getNickname().equals(nickname)) {
                return false;
            }
        } catch (NullPointerException e) {
            return true;
        }
        return true;
    }

    public UserRegisterRespDto registerUser(SignupDto Dto) throws NoSuchAlgorithmException {
        Boolean result = true;
        String err_msg = "회원가입 성공";
        String username = Dto.getUsername();
        String nickname = Dto.getNickname();

        Optional<Users> foundusername = userRepository.findByUsername(username);
        Optional<Users> foundnickname = userRepository.findByNickname(nickname);

        // 회원 ID 중복 확인
        if (foundusername.isPresent()) {
            err_msg = "중복된 사용자 ID가 존재합니다.";
            result = false;
            return new UserRegisterRespDto(result, err_msg);
        }

        // 회원 닉네임 중복 확인
        if (foundnickname.isPresent()) {
            err_msg = "중복된 닉네임이 존재합니다.";
            result = false;
            return new UserRegisterRespDto(result, err_msg);
        }

        // 패스워드 암호화
        String password = bCryptPasswordEncoder.encode(Dto.getPassword());

        Users user = new Users(username, nickname, password);
        System.out.println(Dto.getUsername());
        System.out.println(Dto.getNickname());
        userRepository.save(user);

        UserRegisterRespDto responseDto = new UserRegisterRespDto(result, err_msg);
        return responseDto;
    }

    public Users findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("찾는 회원이 없습니다."));
    }

    public TokenResponseDto socialLogin(String credential) {
        ResponseEntity<SocialLoginRequestDto> response = socialLoginRestTemplate.googleLogin(credential);
        int statusCode = response.getStatusCode().value();

        if (statusCode != 200) {
            throw new RuntimeException("statusCode = " + statusCode);
        }
        SocialLoginRequestDto socialLoginRequestDto = response.getBody();

        Users user = userRepository.findByUsername(socialLoginRequestDto.getEmail())
                .orElseGet(() ->
                        userRepository.save(
                                new Users(
                                        socialLoginRequestDto,
                                        userRepository.findAllCount()
                                )
                        )
                );
        return createTokens(user.getUsername());
    }
}
