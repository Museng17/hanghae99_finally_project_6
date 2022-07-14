package com.hanghae99.finalproject.model.dto.responseDto;

import lombok.*;

@Getter
@Setter
public class FileUploadResponse {

    String fileName;
    String url;

    public FileUploadResponse(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    public FileUploadResponse(String url) {
        this.url = url;
    }
}
