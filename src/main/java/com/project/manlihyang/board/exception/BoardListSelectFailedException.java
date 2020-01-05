package com.project.manlihyang.board.exception;

import com.project.manlihyang.board.BoardConst;

public class BoardListSelectFailedException extends RuntimeException {
    public BoardListSelectFailedException() {
        super(BoardConst.FAILED_NO_BOARD_LIST);
    }
}
