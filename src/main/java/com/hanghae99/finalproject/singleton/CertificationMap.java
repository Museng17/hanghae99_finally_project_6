package com.hanghae99.finalproject.singleton;

import java.time.*;
import java.util.*;

public class CertificationMap {

    private Map<String, String> certificationMap = new HashMap<>();
    private Map<String, LocalDateTime> timeMap = new HashMap<>();
    private Map<String, Boolean> isCertification = new HashMap<>();
    private List<String> usedList = new ArrayList<>();

    private CertificationMap() {
    }

    private static CertificationMap certification = new CertificationMap();

    public static CertificationMap getInstance() {
        return certification;
    }

    /*
     *  Map에 key로 등록할 key값을 첫번째 인자로  value에 등록할 값을 두번째 인자로 주면 put
     * */
    public void put(String key, String value) {
        certificationMap.put(key, value);
        timeMap.put(key, LocalDateTime.now());
        usedList.add(key);
    }

    /*
     *
     *  비밀번호 인증이 성공했을 때 isCertification 에 true 넣는 행위
     * */
    public void put(String key, boolean value) {
        isCertification.put(key, value);
    }


    /*
     *  첫번째 인자로는 Map에 key로 등록한 값은 인자로 주면 map에서 삭제된다.
     *  두번째 인자로는 true이면  isCertification까지 삭제한다.
     * */
    public void remove(String key, boolean isTrue) {
        certificationMap.remove(key);
        timeMap.remove(key);
        if (isTrue) {
            isCertification.remove(key);
        }
    }

    public int getCertificationMapSize() {
        return certificationMap.size();
    }

    /*
     *  Map에 key로 등록한 값을 첫번째 인자에 주고 두번째 인자에 값을 체크할 인자를 넣어주면된다.
     * */
    public boolean match(String key, String value) {
        String certification = certificationMap.get(key);
        if (Optional.ofNullable(certification).isPresent()) {
            if (certification.equals(value)) {
                LocalDateTime startTime = timeMap.get(key);
                Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
                return elapsedTime.getSeconds() < 180;
            }
            return false;
        }
        return false;
    }

    /*
     *
     * 임시비밀번호 발송 하기 전 체크하는 로직
     * Map에 key로 등록한 값을 첫번째 인자에 주고 두번째 인자에 값을 체크할 인자를 넣어주면된다.
     * */
    public boolean match(String key) {
        return Optional.ofNullable(isCertification.get(key)).isPresent();
    }

    public List<String> getUsedList() {
        return this.usedList;
    }

    public void usedListRemove(String email) {
        this.usedList.remove(email);
    }

    public boolean isTimeOver(String key) {
        String certification = certificationMap.get(key);
        if (Optional.ofNullable(certification).isPresent()) {
            LocalDateTime startTime = timeMap.get(key);
            Duration elapsedTime = Duration.between(startTime, LocalDateTime.now());
            return elapsedTime.getSeconds() > 180;
        }
        return true;
    }
}
