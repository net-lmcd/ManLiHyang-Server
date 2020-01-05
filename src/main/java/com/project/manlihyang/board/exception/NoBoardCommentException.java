package com.project.manlihyang.board.exception;

import com.project.manlihyang.board.BoardConst;

public class NoBoardCommentException extends RuntimeException {
    public NoBoardCommentException(){super(BoardConst.FAILED_READ_COMMENT);}
}
