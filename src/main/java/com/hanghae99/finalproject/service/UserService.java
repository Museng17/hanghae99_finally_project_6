package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.exceptionHandler.CustumException.*;
import com.hanghae99.finalproject.jwt.*;
import com.hanghae99.finalproject.model.dto.requestDto.*;
import com.hanghae99.finalproject.model.dto.responseDto.*;
import com.hanghae99.finalproject.model.entity.*;
import com.hanghae99.finalproject.model.repository.*;
import com.hanghae99.finalproject.singleton.CertificationMap;
import com.hanghae99.finalproject.util.restTemplates.SocialLoginRestTemplate;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.hanghae99.finalproject.exceptionHandler.CustumException.ErrorCode.*;
import static com.hanghae99.finalproject.interceptor.JwtTokenInterceptor.JWT_HEADER_KEY;
import static com.hanghae99.finalproject.jwt.JwtTokenProvider.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private CertificationMap certificationMap = CertificationMap.getInstance();

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginRestTemplate socialLoginRestTemplate;
    private final BoardRepository boardRepository;
    private final FolderRepository folderRepository;
    private final UserInfoInJwt userInfoInJwt;
    private final FollowRepository followRepository;
    private final ReportRepository reportRepository;
    private final ImageRepository imageRepository;
    private final ShareRepository shareRepository;

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

    public Boolean checkEmailDuplicate(String email) {
        Users user = userRepository.findByEmail(email).orElse(null);

        try {
            if (user.getEmail().equals(email)) {
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

    @Transactional
    public UserRegisterRespDto registerUser(UserRequestDto Dto) {

        if (!certificationMap.match(Dto.getEmail())) {
            throw new CustomException(ErrorCode.NOT_EMAIL_CERTIFICATION_CHECK);
        }

        //회원가입 정규식 체크
        UserRegisterRespDto valid = joinValid(Dto);
        if (Optional.ofNullable(valid).isPresent()) {
            return valid;
        }

        //회원가입 중복 체크
        valid = duplicateCheck(Dto);
        if (Optional.ofNullable(valid).isPresent()) {
            return valid;
        }

        //암호화
        Dto.setPassword(bCryptPasswordEncoder.encode(Dto.getPassword()));

        //가입
        folderRepository.save(
                new Folder(
                        userRepository.save(
                                new Users(Dto)
                        )
                )
        );

        certificationMap.remove(Dto.getEmail(), true);
        
        return new UserRegisterRespDto(200, true, "회원가입 성공");
    }

    public String findUsername(UserRequestDto userRequestDto) {
        Users user = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FIND_USER));

        return user.getUsername();
    }

    @Transactional
    public UserProfileDto getProfile(long id, HttpServletRequest request) {

        Users user = userFindById(id);
        Users loginUser = userFindById(findUser(request.getAttribute(JWT_HEADER_KEY).toString()).getId());
        return new UserProfileDto(
                user,
                followRepository.findFollowerCountById(id),
                followRepository.findFollowingCountById(id),
                followRepository.findByFollowingIdAndFollowerId(loginUser.getId(), id) != null
        );
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
                .orElseThrow(() -> new CustomException(NOT_FIND_USER));
    }

    @Transactional
    public TokenResponseDto findAccessTokenByCode(String code) {
        SocialLoginRequestDto response = socialLoginRestTemplate.findAccessTokenByCode(code);
        SocialLoginRequestDto socialLoginRequestDto = googleUserInfoByAccessToken(response.getAccess_token());

        Optional<Users> user = userRepository.findByUsername(socialLoginRequestDto.getEmail());

        if (!user.isPresent()) {
            if (!checkEmailDuplicate(socialLoginRequestDto.getEmail())) {
                throw new CustomException(OVERLAP_EMAIL);
            }
            Users users = userRepository.save(new Users(socialLoginRequestDto, new Random().nextInt(7)));
            users.updateNickName();
            folderRepository.save(new Folder(users));
            return createTokens(users.getUsername());
        }

        return createTokens(user.get().getUsername());
    }

    @Transactional
    public TokenResponseDto findAccessTokenByCode2(String code) {
        SocialLoginRequestDto response = socialLoginRestTemplate.findAccessTokenByCode(code);
        SocialLoginRequestDto socialLoginRequestDto = googleUserInfoByAccessToken(response.getAccess_token());

        Optional<Users> user = userRepository.findByUsername(socialLoginRequestDto.getEmail());

        if (!user.isPresent()) {
            if (!checkEmailDuplicate(socialLoginRequestDto.getEmail())) {
                throw new CustomException(OVERLAP_EMAIL);
            }
            Users users = userRepository.save(new Users(socialLoginRequestDto, new Random().nextInt(7)));
            users.updateNickName();
            folderRepository.save(new Folder(users));
            return createTokens2(users.getUsername());
        }

        return createTokens2(user.get().getUsername());
    }

    @Transactional
    public MessageResponseDto UserDelete(HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        List<Board> removeBoardList = boardRepository.findByUsersId(user.getId());

        List<Long> removeBoardIdList = removeBoardList.stream()
                .map(Board::getId)
                .collect(Collectors.toList());
        List<Share> shares = shareRepository.findAllByUsersId(user.getId());

        List<Folder> folders = shares.stream().map(Share::getFolder).collect(Collectors.toList());
        for (Folder folder : folders) {
            folder.setSharedCount(folder.getSharedCount() - 1);
        }

        List<Folder> folderList = folderRepository.findByUsersId(user.getId());
        List<Long> removeFolderIdList = folderList.stream().map(Folder::getId).collect(Collectors.toList());

        shareRepository.deleteByUsersId(user.getId());
        shareRepository.deleteAllByFolderIdIn(removeFolderIdList);
        reportRepository.deleteByReporterId(user.getId());
        reportRepository.deleteByReporterId(user.getId());
        reportRepository.deleteAllByBadfolderIdIn(removeFolderIdList);
        followRepository.deleteByFollowingId(user.getId());
        followRepository.deleteByFollowerId(user.getId());
        imageRepository.deleteByBoardIdIn(removeBoardIdList);
        boardRepository.deleteAllByUsers(user);
        folderRepository.deleteAllByUsers(user);
        userRepository.deleteById(user.getId());

        return new MessageResponseDto(200, "계정 탈퇴 성공");
    }

    public Users userFindById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FIND_USER));
    }

    public SocialLoginRequestDto googleUserInfoByAccessToken(String accessToken) {
        return socialLoginRestTemplate.googleUserInfoByAccessToken(accessToken);
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshToken(String refreshToken) {
        jwtTokenProvider.isHeaderToken(refreshToken);
        jwtTokenProvider.isValidRefreshToken(refreshToken);

        Claims claims = userInfoInJwt.getToken(refreshToken);

        String decodeRefreshToken = (String) claims.get(REFRESH_TOKEN);

        if ((Optional.ofNullable(decodeRefreshToken).isPresent())) {
            if (!decodeRefreshToken.equals(REFRESH_TOKEN)) {
                throw new CustomException(NOT_REFRESH_TOKEN);
            }
        } else {
            throw new CustomException(NOT_REFRESH_TOKEN);
        }

        Users user = findUser((String) claims.get(CLAIMS_KEY));
        return createTokens(user.getUsername());
    }

    @Transactional(readOnly = true)
    public TokenResponseDto refreshToken2(String refreshToken) {
        jwtTokenProvider.isHeaderToken(refreshToken);
        jwtTokenProvider.isValidRefreshToken(refreshToken);

        Claims decodeToken = userInfoInJwt.getToken(refreshToken);

        String decodeRefresh = (String) decodeToken.get(REFRESH_TOKEN);

        if ((Optional.ofNullable(decodeRefresh).isPresent())) {
            if (!decodeRefresh.equals(REFRESH_TOKEN)) {
                throw new CustomException(NOT_REFRESH_TOKEN);
            }
        } else {
            throw new CustomException(NOT_REFRESH_TOKEN);
        }
        Users user = findUser((String) decodeToken.get(CLAIMS_KEY));
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
    public MessageResponseDto updateUserNickname(UserRequestDto userRequestDto, HttpServletRequest request) {
        isNickNameValid(userRequestDto);
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (!checkNameDuplicate(userRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.OVERLAP_NICKNAME);
        }

        user.update(userRequestDto);

        return new MessageResponseDto(200, "계정 닉네임 변경 성공");
    }

    @Transactional
    public void updateUserImg(String url, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        user.updateImg(url);
    }

    @Transactional
    public MessageResponseDto updateUserInfo(UserRequestDto userRequestDto, HttpServletRequest request) {
        if (userRequestDto.getInformation().length() > 40) {
            throw new CustomException(OVER_TEXT);
        }
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        user.updateInfo(userRequestDto);

        return new MessageResponseDto(200, "계정 정보 수정 완료");
    }

    @Transactional
    public MessageResponseDto updateUserPw(UserRequestDto userRequestDto, HttpServletRequest request) {
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", userRequestDto.getPassword())) {
            throw new CustomException(NOT_USE_PASSWORD);
        }

        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            return new MessageResponseDto(501, "기존 비밀번호 확인 실패");
        }

        user.updatePw(bCryptPasswordEncoder.encode(userRequestDto.getNewPassword()));

        return new MessageResponseDto(200, "계정 비밀번호 변경 완료");
    }

    @Transactional
    public MessageResponseDto checkUserPw(UserRequestDto userRequestDto, HttpServletRequest request) {
        Users user = findUser(request.getAttribute(JWT_HEADER_KEY).toString());

        if (!bCryptPasswordEncoder.matches(userRequestDto.getPassword(), user.getPassword())) {
            return new MessageResponseDto(501, "비밀번호가 다릅니다");
        } else {

            return new MessageResponseDto(200, "비밀번호 확인 성공");
        }
    }

    @Transactional(readOnly = true)
    public Users findUserProfile(HttpServletRequest request) {
        return userRepository.findByUsernameNoJoin(request.getAttribute(JWT_HEADER_KEY).toString())
                .orElseThrow(() -> new RuntimeException("찾는 회원이 없습니다."));
    }

    private UserRegisterRespDto joinValid(UserRequestDto dto) {
        if (!Pattern.matches("^[a-zA-Z0-9]{4,11}$", dto.getUsername())) {
            return new UserRegisterRespDto(502, false, "아이디를 다시 확인해주세요");
        }
        if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", dto.getEmail())) {
            return new UserRegisterRespDto(502, false, "이메일을 다시 확인해주세요");
        }
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,}$", dto.getPassword())) {
            return new UserRegisterRespDto(502, false, "패스워드를 다시 확인해주세요");
        }
        return null;
    }

    private UserRegisterRespDto duplicateCheck(UserRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return new UserRegisterRespDto(501, false, "중복된 사용자 ID가 존재합니다.");
        }
        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
            return new UserRegisterRespDto(501, false, "중복된 닉네임이 존재합니다.");
        }

        if (!checkEmailDuplicate(dto.getEmail())) {
            return new UserRegisterRespDto(501, false, "중복된 이메일이 존재합니다.");
        }
        return null;
    }

    private void isNickNameValid(UserRequestDto userRequestDto) {
        if (userRequestDto.getNickname().trim().replace(" ", "").startsWith("운영자") ||
                userRequestDto.getNickname().trim().replace(" ", "").startsWith("admin")) {
            throw new CustomException(CNT_NOT_USE_NICKNAME);
        }

        if (userRequestDto.getNickname().startsWith("익명의 사용자")) {
            try {
                Integer.parseInt(userRequestDto.getNickname().trim().substring(7));
                throw new CustomException(CNT_NOT_USE_NICKNAME);
            } catch (NumberFormatException e) {
            } finally {
                log.info("UserService.updateUserName : " + userRequestDto.getNickname());
            }
        }

        if (userRequestDto.getNickname().trim().length() > 10 || userRequestDto.getNickname().trim().length() <= 0) {
            throw new CustomException(OVER_TEXT);
        }
    }

}
