package com.project.manlihyang.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.manlihyang.board.controller.BoardController;
import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.repository.BoardRepository;
import com.project.manlihyang.util.ApiHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    AmazonS3 amazonS3Client;

    @Value("${spring.aws.bucket}")
    private String bucket;

    //list
    public ArrayList<Board> BoardsReadService( ) {
        return boardDao.BoardsReadRepo();
    }

    public Board BoardReadDetailService(String bsn) {
        return boardDao.BoardReadDetailRepo(bsn);
    }

    public String BoardCreateService(Board board, MultipartFile file) {

        String bsn = UUID.randomUUID().toString();
        String img_url = null;
        String img_name = null;
        PutObjectRequest putObjectRequest = null;
        try {
            img_name = file.getOriginalFilename() + "." + apiHelper.makeTimeStamp();
            putObjectRequest = new PutObjectRequest(bucket, img_name, apiHelper.convertMultiPartToFile(file))
                                            .withCannedAcl(CannedAccessControlList.PublicRead);// 퍼블릭으로 공개하여 s3에 올림.
            amazonS3Client.putObject(putObjectRequest);
            img_url = amazonS3Client.getUrl(bucket, img_name).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            board.setBsn(bsn); // bsn 세팅
            board.setGroup_id(bsn); // 게시물 원본일 경우 group_id는 자기 자신
            board.setCreated_time(apiHelper.makeNowTimeStamp());
            board.setUpdated_time(apiHelper.makeNowTimeStamp());
            board.setImg_url(img_url);
            board.setImg_name(img_name);
            boardDao.BoardCreateRepo(board);

            return bsn;
        } catch (Exception e) {
            logger.error("[BoardService] createNewBoard() ERROR : " + e.getMessage());
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
