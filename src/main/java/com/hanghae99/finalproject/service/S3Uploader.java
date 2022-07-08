package com.hanghae99.finalproject.service;

import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import com.hanghae99.finalproject.model.dto.responseDto.FileUploadResponse;
import com.hanghae99.finalproject.model.entity.Users;
import com.hanghae99.finalproject.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

import static com.hanghae99.finalproject.util.resultType.FileUpload.UBUNTU_BASE_PATH;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;

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

    /*게시글 작성시 이미지 업로드*/
    public FileUploadResponse upload(MultipartFile uploadFile, String path) {
        File imageFile = multipartFileToFile(uploadFile);
        String imgName = path + makeFileName(uploadFile);
        String uploadImageUrl = putS3(imageFile, imgName);
        removeNewFile(imageFile);
        return new FileUploadResponse(uploadImageUrl);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile == null)

            if (targetFile.delete()) {
                log.info("파일이 삭제되었습니다.");
                return;
            }
        log.info("파일 삭제 실패");
    }

    /*S3에서 이미지 찾기*/
    public S3Object selectImage(String path, String imageName) {
        return amazonS3.getObject(new GetObjectRequest(bucket, path + "/" + imageName));
    }

    /* 파일이름 재설정 */
    public String makeFileName(MultipartFile attcFile) {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        String attcFileOriNm = attcFile.getOriginalFilename();
        String attcFileOriExt = attcFileOriNm.substring(attcFileOriNm.lastIndexOf("."));
        return attcFileNm + attcFileOriExt;
    }

    /* multipartFileToFile하는 작업 */
    private File multipartFileToFile(MultipartFile uploadFile) {
        File file = null;
        FileOutputStream fos = null;
        try {
            file = imageUploadToSever(uploadFile);
            file.createNewFile();
            fos = new FileOutputStream(file);
            fos.write(uploadFile.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("S3Uploader 121 에러" + e.getMessage());
        } finally {
            close(fos);
        }
        return file;
    }

    /*우분투 환경와 window환경에서 이미지업로드 */
    private File imageUploadToSever(MultipartFile uploadFile) {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.startsWith("windows")) {
            return new File(new File("").getAbsolutePath() + "\\" + uploadFile.getOriginalFilename());
        }

        File file = new File(UBUNTU_BASE_PATH.getPath());
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(UBUNTU_BASE_PATH.getPath() + "/" + uploadFile.getOriginalFilename());
    }

    /* close 예외처리*/
    private void close(FileOutputStream fos) {
        if (fos == null)
            return;
        try {
            fos.close();
        } catch (Exception e) {
            throw new RuntimeException("S3Uploader 137 에러" + e.getMessage());
        }
    }
}
