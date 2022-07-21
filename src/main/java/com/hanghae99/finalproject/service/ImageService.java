package com.hanghae99.finalproject.service;

import com.hanghae99.finalproject.model.dto.responseDto.MassageResponseDto;
import com.hanghae99.finalproject.model.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.hanghae99.finalproject.model.resultType.FileUploadType.BOARD;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    public MassageResponseDto imageUpload(MultipartFile multipartFile) {
        return new MassageResponseDto(200, "업로드 완료 했습니다.", s3Uploader.upload(multipartFile, BOARD.getPath()).getUrl());
    }
}
