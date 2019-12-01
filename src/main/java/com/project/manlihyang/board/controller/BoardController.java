package com.project.manlihyang.board.controller;

import com.project.manlihyang.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String board_list( ) {
        return boardService.ReadBoards();
    }
}
