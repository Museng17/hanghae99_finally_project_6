package com.hanghae99.finalproject.scheduled;

import com.hanghae99.finalproject.singleton.CertificationMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ListIterator;

@Slf4j
public class MailScheduled {

    private CertificationMap certificationMap = CertificationMap.getInstance();

    @Scheduled(cron = "00 00 10 * * *")
    private void singletonRemove() {

        
        log.info("스케쥴러 작동");
        log.info("삭제되기전 인증요청 카운트 : " + certificationMap.getCertificationMapSize());
        int checkCnt = 0;
        try {
            ListIterator<String> listIterator = certificationMap.getUsedList().listIterator();
            for (ListIterator<String> iterator = listIterator; iterator.hasNext(); ) {
                String key = iterator.next();
                if (certificationMap.isTimeOver(key)) {
                    certificationMap.remove(key, true);
                    iterator.remove();
                    checkCnt++;
                }
            }
            log.info("스케쥴러 작동 완료 삭제된 데이터 카운트 : " + checkCnt);
        } catch (Exception e) {
            log.info("스케쥴러 작동 실패 에러 메세지 : ");
            log.info(e.toString());
        }
    }
}
