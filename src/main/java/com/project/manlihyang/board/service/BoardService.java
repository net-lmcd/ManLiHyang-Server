package com.project.manlihyang.board.service;

import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardDao;

    //list
    public ArrayList<Board> ReadBoards( ) {
        return boardDao.ReadBoards();
    }

    public int CreateBoard(Board board) {
        return boardDao.CreateBoard(board);
    };
}
