package com.hanghae99.finalproject.model.resultType;

import lombok.*;

@Getter
@NoArgsConstructor
public enum GoogleLoginType {
    GOOGLE_ACCESS_TOKEN_URL("googleAccessTokenUrl", "accounts.google.com"),
    GOOGLE_USER_INFO_URL("googleUserInfoUrl", "www.googleapis.com"),
    GOOGLE_ACCESS_PATH("path", "/o/oauth2/token"),
    GOOGLE_USER_INFO_PATH("path", "/oauth2/v3/tokeninfo"),
    ACCESS_TOKEN("access_token"),
    PROTOCOL("protocol", "https"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    CODE("code"),
    GRANT_TYPE("grant_type", "authorization_code"),
    REDIRECT_URI("redirect_uri", "https://moum.cloud");

    private String name;
    private String value;

    GoogleLoginType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    GoogleLoginType(String name) {
        this.name = name;
    }
}
