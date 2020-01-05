package com.project.manlihyang.board.exception;

import com.project.manlihyang.board.BoardConst;

public class NoBoardLikeException extends RuntimeException {
    public NoBoardLikeException( ) { super(BoardConst.No_BOARD_LIKE);}
}
