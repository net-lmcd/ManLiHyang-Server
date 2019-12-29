package com.project.manlihyang.board.service;

import com.project.manlihyang.board.controller.BoardController;
import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.repository.BoardRepository;
import com.project.manlihyang.util.ApiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private ApiHelper apiHelper;

    @Autowired
    private BoardRepository boardDao;

    //list
    public ArrayList<Board> BoardsReadService( ) {
        return boardDao.BoardsReadRepo();
    }

    public Board BoardReadDetailService(String bsn) {
        return boardDao.BoardReadDetailRepo(bsn);
    }

    public String BoardCreateService(Board board) {

        String bsn = UUID.randomUUID().toString();
        try {
            board.setBsn(bsn);
            board.setGroup_id(bsn); // 게시물 원본일 경우 group_id는 자기 자신
            boardDao.BoardCreateRepo(board);
            board.setCreated_time(apiHelper.makeNowTimeStamp());
            board.setUpdated_time(apiHelper.makeNowTimeStamp());
            return bsn;
        } catch (Exception e) {
            logger.error("[UserService] createNewUser() ERROR : " + e.getMessage());
        }
        return "-1";

    };
    public int BoardUpdateService(Board board){
        board.setUpdated_time(apiHelper.makeNowTimeStamp());
        return boardDao.BoardUpdateRepo(board);
    };
    public int BoardDeleteService(String bsn) { return boardDao.BoardDeleteRepo(bsn); };

    //좋아요
    public int BoardCheckLikeService(String board_id, String liker_id) { return boardDao.BoardCheckLikeRepo(board_id, liker_id);}
    //좋아요 취소
    public int BoardCancelLikeService(String board_id, String liker_id) {return boardDao.BoardCancelLikeRepo(board_id, liker_id);}
    //좋아요 횟수 및 유저 리스트
    public List<Integer> BoardDetailLikeService(String bsn) { return boardDao.BoardDetailLikeRepo(bsn);}

    //게시물 신고하기
    public int BoardReportService(String bsn, int report_cnt) {return boardDao.BoardReportRepo(bsn, report_cnt);}
    //5번 이상 신고된 게시물 삭제
    public int BoardReportDelService(String bsn) {return boardDao.BoardReportDelRepo(bsn);}
}
