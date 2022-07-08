package com.hanghae99.finalproject.util.resultType;

import lombok.*;

@Getter
@NoArgsConstructor
public enum FileUploadType {
    PROFILE("profile/"),
    BOARD("board/"),
    UBUNTU_BASE_PATH("/home/ubuntu");

    private String path;

    FileUploadType(String path) {
        this.path = path;
    }
}
