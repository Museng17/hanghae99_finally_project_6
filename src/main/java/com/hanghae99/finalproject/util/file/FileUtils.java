package com.hanghae99.finalproject.util.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Component
public class FileUtils {

    /* MultipartFile 데이터 타입 파일이름 재설정 */
    public String makeFileName(MultipartFile attcFile) {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        String attcFileOriNm = attcFile.getOriginalFilename();
        String attcFileOriExt = fileExtCheck(attcFileOriNm.substring(attcFileOriNm.lastIndexOf(".")));
        return attcFileNm + attcFileOriExt;
    }

    /* File데이터타입 파일이름 재설정 */
    public String makeFileName(File attcFile) {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        String attcFileOriNm = attcFile.getName();
        String attcFileOriExt = fileExtCheck(attcFileOriNm.substring(attcFileOriNm.lastIndexOf(".")));
        return attcFileNm + attcFileOriExt;
    }

    /* OG태크 데이터타입  파일이름 재설정 */
    public String makeFileName() {
        String attcFileNm = UUID.randomUUID().toString().replaceAll("-", "");
        return attcFileNm + ".png";
    }

    public String fileExtCheck(String originalFileExtension) {
        originalFileExtension = originalFileExtension.toLowerCase();
        if (originalFileExtension.equals(".jpg") || originalFileExtension.equals(".gif")
                || originalFileExtension.equals(".png") || originalFileExtension.equals(".jpeg")
                || originalFileExtension.equals(".bmp")) {
            return originalFileExtension;
        }
        log.info("FileUtils.fileExtCheck : "+ originalFileExtension + "는 지원하지 않습니다.");
        throw new RuntimeException(originalFileExtension + "는 지원하지 않습니다.");
    }
}
