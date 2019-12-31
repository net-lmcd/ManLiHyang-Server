package com.project.manlihyang.util;

import com.project.manlihyang.common.RequestData;
import com.project.manlihyang.common.exception.NoServiceException;
import org.springframework.stereotype.Component;
import sun.misc.Request;

import java.util.regex.Pattern;

@Component
public class Validator {
    private static final Pattern E_MAIL = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");

    /**
     * Validate Email Address by regex
     * @param email email
     * @return boolean
     */
    public static void isValidEmailAddress(String email) {
        boolean isValid = E_MAIL.matcher(email).matches();
        if (!isValid) {
            throw new RuntimeException("이메일 형식이 유효하지 않습니다.");
        }
    }

    /**
     * service code 체크
     * @param data
     * @param <T>
     */
    public <T> void checkValidUserCode(T data) {
        if (data instanceof RequestData) {
            RequestData requestData = (RequestData) data;
            if (requestData.getServiceCode() != 1000) {
                throw new NoServiceException();
            }
        }
    }

    public void checkValidUserServiceCode(int code) {
        if (code != 1000)
            throw new NoServiceException();
    }

    public void checkValidBoardServiceCode(int code) {
        if(code != 1001)
            throw new NoServiceException();
    }
}