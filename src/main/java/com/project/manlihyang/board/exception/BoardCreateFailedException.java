package com.project.manlihyang.board.exception;

import com.project.manlihyang.user.UserConst;

public class BoardCreateFailedException extends RuntimeException{
    public BoardCreateFailedException() {
        super(ExceptionValue.FAILED_CREATE_BOARD);
    }
}
