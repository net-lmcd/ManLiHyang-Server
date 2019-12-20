package com.project.manlihyang.util;

import org.springframework.stereotype.Component;

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
}