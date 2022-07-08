package com.hanghae99.finalproject.util.file;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class FileUtils {

    /* MultipartFile 데이터 타입 파일이름 재설정 */
    public String makeFileName(MultipartFile attcFile) {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        String attcFileOriNm = attcFile.getOriginalFilename();
        String attcFileOriExt = attcFileOriNm.substring(attcFileOriNm.lastIndexOf("."));
        return attcFileNm + attcFileOriExt;
    }

    /* File데이터타입 파일이름 재설정 */
    public String makeFileName(File attcFile) {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        String attcFileOriNm = attcFile.getName();
        String attcFileOriExt = attcFileOriNm.substring(attcFileOriNm.lastIndexOf("."));
        return attcFileNm + attcFileOriExt;
    }

    /* String데이터타입  파일이름 재설정 */
    public String makeFileName(String imagePath) {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        String attcFileOriExt = imagePath.substring(imagePath.lastIndexOf("."));
        return attcFileNm + attcFileOriExt;
    }
}
