package com.hanghae99.finalproject.util;

import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class UserinfoHttpRequest {

    private final UserService userService;

    public void userAndWriterMatches(Long boardUserId, Long userId) {
        if (!boardUserId.equals(userId)) {
            throw new RuntimeException("글쓴이가 아닙니다.");
        }
    }

    public Users userFindByToken(HttpServletRequest request) {
        return userService.findUser(request.getAttribute("Authorization").toString());
    }
}
