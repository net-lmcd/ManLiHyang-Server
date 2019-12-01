package com.project.manlihyang.board.service;

import com.project.manlihyang.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardDao;

    //list
    public String ReadBoards( ) {
        return boardDao.getCurrentDateTime();
    }

}
