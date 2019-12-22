package com.project.manlihyang.user.exception;

import com.project.manlihyang.user.UserConst;

public class NoEmailException extends RuntimeException {
    public NoEmailException() {
        super(UserConst.NO_EMAIL_FORMAT);
    }
}
