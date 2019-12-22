package com.project.manlihyang.user.exception;

import com.project.manlihyang.user.UserConst;

public class CreateUserFailedException extends RuntimeException {
    public CreateUserFailedException() {
        super(UserConst.FAILED_CREATE_USER);
    }
}
