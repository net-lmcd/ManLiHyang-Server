package com.project.manlihyang.board.exception;

public class BoardCreateFailedException extends RuntimeException{
    public BoardCreateFailedException() {
        super(BoardConst.FAILED_CREATE_BOARD);
    }
}
