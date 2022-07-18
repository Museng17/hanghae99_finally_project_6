package com.hanghae99.finalproject.singleton;

import java.util.*;

public class CertificationMap {

    private static Map<String, String> certificationList = new HashMap<>();

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
        certificationList.put(key, value);
    }

    /*
     *  Map에 key로 등록한 값은 인자로 주면 map에서 삭제된다.
     * */
    public void remove(String email) {
        certificationList.remove(email);
    }

    /*
     *  Map에 key로 등록한 값을 첫번째 인자에 주고 두번째 인자에 값을 체크할 인자를 넣어주면된다.
     * */
    public boolean match(String key, String checkValue) {
        String certification = certificationList.get(key);
        if (Optional.ofNullable(certification).isPresent()) {
            return certification.equals(checkValue);
        }
        return false;
    }
}
