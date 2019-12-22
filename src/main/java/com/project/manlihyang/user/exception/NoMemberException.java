package com.project.manlihyang.user.exception;

import com.project.manlihyang.user.UserConst;

/**
 * [phantasmicmeans] created on 22/12/2019
 */
public class NoMemberException extends RuntimeException{
    public NoMemberException() {
        super(UserConst.NO_MEMBER);
    }
}
