package com.project.manlihyang.board.exception;

import com.project.manlihyang.board.BoardConst;

public class NoBoardException extends RuntimeException {
    public NoBoardException() {
        super(BoardConst.FAILED_NO_BOARD_DETAIL);
    }}
