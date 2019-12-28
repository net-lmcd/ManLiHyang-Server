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
    public ArrayList<Board> ReadBoardsService( ) {
        return boardDao.ReadBoardsRepo();
    }

    public Board ReadBoardDetailService(int board_id) {
        return boardDao.ReadBoardDetailRepo(board_id);
    }

    public int CreateBoardService(Board board) {
        return boardDao.CreateBoardRepo(board);
    };
    public int UpdateBoardService(Board board){ return boardDao.UpdateBoardRepo(board); };
    public int DeleteBoardService(int board_id) { return boardDao.DeleteBoardRepo(board_id); };

    //좋아요
    public int CheckBoardLikeService(int board_id, int liker_id) { return boardDao.CheckBoardLikeRepo(board_id, liker_id);}
    //좋아요 취소
    public int CancelBoardLikeService(int board_id, int liker_id) {return boardDao.CancelBoardLikeRepo(board_id, liker_id);}
    //좋아요 횟수 및 유저 리스트
    public List<Integer> DetailBoardLikeService(int board_id) { return boardDao.DetailBoardLikeRepo(board_id);}
}
