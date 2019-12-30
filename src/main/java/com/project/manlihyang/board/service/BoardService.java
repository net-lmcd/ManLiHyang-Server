package com.project.manlihyang.board.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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

    //게시물 조회
    public ArrayList<Board> BoardsReadService( ) {
        return boardDao.BoardsReadRepo();
    }
    //게시물 상세조회
    public Board BoardReadDetailService(String bsn) {
        return boardDao.BoardReadDetailRepo(bsn);
    }
    //게시물 생성
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
    //게시물 수정
    public int BoardUpdateService(Board board){
        board.setUpdated_time(apiHelper.makeNowTimeStamp());
        return boardDao.BoardUpdateRepo(board);
    };
    //게시물 삭
    public int BoardDeleteService(String bsn) {
        //해당 bsn의 s3 이미지 삭제. ( 인자로 받은 bsn 값을 갖는 게시물의 img_name 필요 )
        String filename = boardDao.BoardImgNameRepo(bsn);
        try { // s3에 있는 이미지 file 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filename));
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + filename + "] ");
        }
        return boardDao.BoardDeleteRepo(bsn);
    };

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

    //댓글 달기
    public String BoardCommentService(Board comment) {

        String bsn = UUID.randomUUID().toString();
        //부모의 group_id, group_seq, group_depth
        int parent_group_seq = comment.getGroup_seq();
        int parnet_group_depth = comment.getGroup_depth();
        String group_id = comment.getGroup_id();
        try {
            boardDao.BoardCommentUpdateGroupSeqRepo(group_id, parent_group_seq);
        } catch (Exception e) {
            logger.error("[BoardService] createNewComment() UPDATE ERROR : " + e.getMessage());
        }

        try {
            comment.setBsn(bsn); // bsn 세팅

            //내가 들어갈 자리는 부모 위치에서 1을 더해줌 ( seq, depth )
            int my_group_seq = parent_group_seq + 1;
            int my_group_depth = parnet_group_depth + 1;
            comment.setGroup_seq(my_group_seq);
            comment.setGroup_depth(my_group_depth);
            boardDao.BoardCommentCreateRepo(comment);
            return bsn;
        } catch (Exception e) {
            logger.error("[BoardService] createNewComment() INSERT ERROR : " + e.getMessage());
        }
        return "-1";
    }

    //댓글 조회
    public ArrayList<Board> BoardCommentListService(String bsn) {
         return boardDao.BoardCommentsReadRepo(bsn);
    }

    //댓글 수정
    public String BoardCommentUpdateService(Board comment) {

        String bsn = comment.getBsn();
        try {
            comment.setUpdated_time(apiHelper.makeNowTimeStamp());
            boardDao.BoardCommentUpdateRepo(comment);
            return bsn;
        } catch (Exception e) {
            logger.error("[BoardService] updateComment() : " + e.getMessage());
        }
        return "-1";
    }

    //댓글 삭제 -> 컬럼을 지우는게 아니라 컬럼은 남기고 is_del 만 true로 바꾸고 내용은 "해당 내용이 삭제되었습니다."라고 바
    public String BoardCommentDeleteService(String bsn) {
        try {
            boardDao.BoardCommentDeleteRepo(bsn);
            return bsn;
        } catch (Exception e) {
            logger.error("[BoardService] deleteComment() : " + e.getMessage());
        }
        return "-1";
    }
}
