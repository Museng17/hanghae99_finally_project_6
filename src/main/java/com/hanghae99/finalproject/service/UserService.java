package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.jwt.*;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.util.restTemplates.SocialLoginRestTemplate;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.hanghae99.finalproject.interceptor.JwtTokenInterceptor.JWT_HEADER_KEY;
import static com.hanghae99.finalproject.jwt.JwtTokenProvider.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginRestTemplate socialLoginRestTemplate;
    private final BoardRepository boardRepository;
    private final FolderRepository folderRepository;
    private final UserInfoInJwt userInfoInJwt;

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserRequestDto userRequestDto) {
        Users user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("회원가입되지 않은 아이디입니다."));

        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }
        return createTokens(user.getUsername());
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

    public UserRegisterRespDto registerUser(UserRequestDto Dto) throws NoSuchAlgorithmException {
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

    @Transactional
    public TokenResponseDto findAccessTokenByCode(String code) {
        SocialLoginRequestDto response = socialLoginRestTemplate.findAccessTokenByCode(code);
        SocialLoginRequestDto socialLoginRequestDto = googleUserInfoByAccessToken(response.getAccess_token());

        Users user = userRepository.findByUsername(socialLoginRequestDto.getEmail())
                .orElseGet(() ->
                        userRepository.save(
                                new Users(
                                        socialLoginRequestDto,
                                        userRepository.findAllCount()
                                )
                        )
                );
        return createTokens2(user.getUsername());
    }

    @Transactional
    public Boolean UserDelete(Long id, HttpServletRequest request) {
        Users user = userFindById(id);
        if (user.getId() == findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId()) {

            boardRepository.deleteAllByUsers(user);
            folderRepository.deleteAllByUsers(user);
            userRepository.deleteById(id);

            return true;
        } else {

            return false;
        }
    }

    public Users userFindById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
    }

    public SocialLoginRequestDto googleUserInfoByAccessToken(String accessToken) {
        return socialLoginRestTemplate.googleUserInfoByAccessToken(accessToken);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshToken(String refreshToken) {
        jwtTokenProvider.validToken(refreshToken);
        Claims decodeToken = userInfoInJwt.getRefreshToken(refreshToken);

        String decodeRefresh = (String) decodeToken.get(REFRESH_TOKEN);

        if ((Optional.ofNullable(decodeRefresh).isPresent())) {
            if (!decodeRefresh.equals(REFRESH_TOKEN)) {
                throw new RuntimeException(refreshToken + "는 " + REFRESH_TOKEN + "이 아닙니다.");
            }
        } else {
            throw new RuntimeException(REFRESH_TOKEN + "이 아닙니다.");
        }

        return createTokens(findUser((String) decodeToken.get(CLAIMS_KEY)).getUsername());
    }

    public TokenResponseDto createTokens(String username) {
        return new TokenResponseDto(
                jwtTokenProvider.createAccessToken(username),
                jwtTokenProvider.createRefreshToken(username)
        );
    }

    public TokenResponseDto createTokens2(String username) {
        return new TokenResponseDto(
                jwtTokenProvider.createAccessToken2(username),
                jwtTokenProvider.createRefreshToken2(username)
        );
    }
    
    @Transactional
    public Boolean updateUserInfo(Long id, UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = userFindById(id);
        if (!checkNameDuplicate(userRequestDto.getNickname())) {
            throw new RuntimeException("닉네임이 중복되었습니다.");
        }

        if (user.getId() == findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId()) {
            user.update(userRequestDto);

            return true;
        } else {

            return false;
        }
    }

    @Transactional
    public Boolean updateUserPw(Long id, UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = userFindById(id);
        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        if (user.getId() == findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId()) {
            user.updatePw(bCryptPasswordEncoder.encode(userRequestDto.getNewPassword()));

            return true;
        } else {

            return false;
        }
    }

    @Transactional(readOnly = true)
    public Users findUserProfile(HttpServletRequest request) {
        return findUser(request.getAttribute(JWT_HEADER_KEY).toString());
    }
}
