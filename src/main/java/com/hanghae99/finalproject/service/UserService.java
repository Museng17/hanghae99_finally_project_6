package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.jwt.*;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.*;
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
import java.util.regex.Pattern;

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
    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public TokenResponseDto login(UserRequestDto userRequestDto) {
        Users user = userRepository.findByUsername(userRequestDto.getUsername())
                .orElseThrow(() -> new RuntimeException("UserService 38에러 회원가입되지 않은 아이디입니다."));

        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("UserService 41 에러 비밀번호가 틀렸습니다.");
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

    public static boolean isEmail(String s) {
        return Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", s);
    }

    public static boolean isPassword(String s) {
        return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", s);
    }

    public UserRegisterRespDto registerUser(UserRequestDto Dto) throws NoSuchAlgorithmException {
        Boolean result = true;
        String err_msg = "회원가입 성공";
        String username = Dto.getUsername();
        String nickname = Dto.getNickname();

        if (!isEmail(username)) {
            err_msg = "잘못된 이메일 양식입니다.";
            result = false;
            return new UserRegisterRespDto(result, err_msg);
        }

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

        String pw = Dto.getPassword();
        if (!isPassword(pw)) {
            err_msg = "잘못된 비밀번호 입니다.";
            result = false;
            return new UserRegisterRespDto(result, err_msg);
        }

        // 패스워드 암호화
        String password = bCryptPasswordEncoder.encode(pw);

        Users user = new Users(username, nickname, password);
        System.out.println(Dto.getUsername());
        System.out.println(Dto.getNickname());
        userRepository.save(user);
        Users user1 = userRepository.save(user);
        folderRepository.save(
                new Folder(
                        user1
                )
        );

        return new UserRegisterRespDto(result, err_msg);
    }

    @Transactional
    public UserProfileDto getProfile(long id, HttpServletRequest request) {
        UserProfileDto userProfileDto = new UserProfileDto();
        Users user = userFindById(id);
        Users loginUser = userFindById(findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId());
        userProfileDto = new UserProfileDto(
                user,
                followRepository.findFollowerCountById(id),
                followRepository.findFollowingCountById(id),
                followRepository.findByFollowingIdAndFollowerId(loginUser.getId(), id) != null
        );
        return userProfileDto;
    }

    @Transactional
    public MyProfileDto getMyProfile(HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());
        return new MyProfileDto(
                user,
                followRepository.findFollowerCountById(user.getId()),
                followRepository.findFollowingCountById(user.getId())
        );
    }

    public Users findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("UserService 109 에러 찾는 회원이 없습니다."));
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

        folderRepository.save(
                new Folder(
                        user
                )
        );
        return createTokens(user.getUsername());
    }

    @Transactional
    public TokenResponseDto findAccessTokenByCode2(String code) {
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

        folderRepository.save(
                new Folder(
                        user
                )
        );
        return createTokens2(user.getUsername());
    }

    @Transactional
    public Boolean UserDelete(HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (user.getId() == findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId()) {

            boardRepository.deleteAllByUsers(user);
            folderRepository.deleteAllByUsers(user);
            userRepository.deleteById(user.getId());

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
                throw new RuntimeException(refreshToken + "는 " + REFRESH_TOKEN + "이 아닙니다.  UserService + 163에러 ");
            }
        } else {
            throw new RuntimeException(REFRESH_TOKEN + "이 아닙니다. UserService 166에러 ");
        }

        return createTokens(findUser((String) decodeToken.get(CLAIMS_KEY)).getUsername());
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshToken2(String refreshToken) {
        jwtTokenProvider.validToken(refreshToken);
        Claims decodeToken = userInfoInJwt.getRefreshToken(refreshToken);

        String decodeRefresh = (String) decodeToken.get(REFRESH_TOKEN);

        if ((Optional.ofNullable(decodeRefresh).isPresent())) {
            if (!decodeRefresh.equals(REFRESH_TOKEN)) {
                throw new RuntimeException(refreshToken + "는 " + REFRESH_TOKEN + "이 아닙니다.  UserService + 163에러 ");
            }
        } else {
            throw new RuntimeException(REFRESH_TOKEN + "이 아닙니다. UserService 166에러 ");
        }
        return createTokens2(findUser((String) decodeToken.get(CLAIMS_KEY)).getUsername());
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
    public Boolean updateUserName(UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());
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
    public void updateUserImg(String url, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (user.getId() == findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId()) {
            user.updateImg(url);
        }
    }

    @Transactional
    public Boolean updateUserInfo(UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (user.getId() == findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId()) {
            user.updateInfo(userRequestDto);

            return true;
        } else {

            return false;
        }
    }

    @Transactional
    public Boolean updateUserPw(UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

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

    @Transactional
    public Boolean checkUserPw(UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호와 다릅니다.");
        } else {

            return true;
        }
    }

    @Transactional(readOnly = true)
    public Users findUserProfile(HttpServletRequest request) {
        return userRepository.findByUsernameNoJoin(request.getAttribute(JWT_HEADER_KEY).toString())
                .orElseThrow(() -> new RuntimeException("찾는 회원이 없습니다."));
    }
}
