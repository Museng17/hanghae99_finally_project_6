package com.hanghae99.finalproject.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import com.hanghae99.finalproject.model.dto.responseDto.FileUploadResponse;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket; // S3 버킷 이름

    public FileUploadResponse upload(Long userId, MultipartFile multipartFile, String dirName) throws IOException {

        File uploadFile = new File(System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
        if (uploadFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            FileOutputStream fos = new FileOutputStream(uploadFile);
            fos.write(multipartFile.getBytes());
        }

        return upload(userId, uploadFile, dirName);
    }

    private FileUploadResponse upload(Long userId, File uploadFile, String dirName) {
        String imgName = dirName + "/" + UUID.randomUUID().toString() + uploadFile.getName().substring(uploadFile.getName().lastIndexOf("."));
        String uploadImageUrl = putS3(uploadFile, imgName);
        removeNewFile(uploadFile);

        Users user = userRepository.findById(userId).get();
        user.setImgPath(uploadImageUrl);

        return new FileUploadResponse(imgName, uploadImageUrl);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
            return;
        }
        log.info("파일 삭제 실패");
    }

}
