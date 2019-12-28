package com.project.manlihyang.board.service;

import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardDao;

    //list
    public ArrayList<Board> BoardsReadService( ) {
        return boardDao.BoardsReadRepo();
    }

    public Board BoardReadDetailService(int board_id) {
        return boardDao.BoardReadDetailRepo(board_id);
    }

    public int BoardCreateService(Board board) {
        return boardDao.BoardCreateRepo(board);
    };
    public int BoardUpdateService(Board board){ return boardDao.BoardUpdateRepo(board); };
    public int BoardDeleteService(int board_id) { return boardDao.BoardDeleteRepo(board_id); };

    //좋아요
    public int BoardCheckLikeService(int board_id, int liker_id) { return boardDao.BoardCheckLikeRepo(board_id, liker_id);}
    //좋아요 취소
    public int BoardCancelLikeService(int board_id, int liker_id) {return boardDao.BoardCancelLikeRepo(board_id, liker_id);}
    //좋아요 횟수 및 유저 리스트
    public List<Integer> BoardDetailLikeService(int board_id) { return boardDao.BoardDetailLikeRepo(board_id);}

    //게시물 신고하기
    public int BoardReportService(int board_id, int report_cnt) {return boardDao.BoardReportRepo(board_id, report_cnt);}
    //5번 이상 신고된 게시물 삭제
    public int BoardReportDelService(int board_id) {return boardDao.BoardReportDelRepo(board_id);}
}
