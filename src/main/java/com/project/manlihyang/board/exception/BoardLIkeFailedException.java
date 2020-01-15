package com.project.manlihyang.board.exception;

import com.project.manlihyang.board.BoardConst;

public class BoardLIkeFailedException extends RuntimeException{
    public BoardLIkeFailedException() {super(BoardConst.FAILED_BOARD_LIKE);}
}
