package com.project.manlihyang.board.exception;

import com.project.manlihyang.board.BoardConst;

public class BoardCreateFailedException extends RuntimeException{
    public BoardCreateFailedException() {
        super(BoardConst.FAILED_CREATE_BOARD);
    }
}
