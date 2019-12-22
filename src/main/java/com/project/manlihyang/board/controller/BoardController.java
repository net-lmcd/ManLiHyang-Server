package com.project.manlihyang.board.controller;

import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    //게시물 조회 ( 전체 )
    @GetMapping("")
    public ArrayList<Board> board_read( ) {
        return boardService.ReadBoards();
    }

    //특정 게시물 상세조회
    @GetMapping("/{id}")
    public void board_read_detail(@PathVariable("id") String board_id){

    }

    //게시물 삽입
    @PostMapping("")
    public int board_insert(Board board) {
        return boardService.CreateBoard(board);
    }

    //게시물 수정

    //게시물 삭제

    //게시물 좋아요

    //게시물 신고

    //댓글 조회

    //댓글 삽입

    //댓글 삭제

    //댓글 수정
