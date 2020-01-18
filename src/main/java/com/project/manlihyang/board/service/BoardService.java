package com.project.manlihyang.board.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.manlihyang.board.controller.BoardController;
import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.BoardConst;
import com.project.manlihyang.board.exception.BoardLIkeFailedException;
import com.project.manlihyang.board.repository.BoardRepository;
import com.project.manlihyang.util.ApiHelper;
import com.project.manlihyang.util.Validator;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private ApiHelper apiHelper;

    @Autowired
    private BoardRepository boardDao;

    @Autowired
    private Validator validator;

    @Autowired
    AmazonS3 amazonS3Client;

    @Value("${spring.aws.bucket}")
    private String bucket;

    //게시물 배경 이미지 조회 ( default )
    public Map<String, String> BoardBackGroundImgListService(String type) {
        return s3_file_list(type);
    }

    //게시물 책 이미지 조회 ( default )
    public Map<String, String> BoardBookImgListService(String type) {
        return s3_file_list(type);
    }


    //게시물 전체 조회 ( 최신순 )
    public List<Board> BoardsNewestReadService( ) {
        List<Board> boards = null;
        try {
            boards = boardDao.BoardsReadNewestRepo();
        } catch (Exception e) {
            logger.error("[BoardService] BoardsReadService() ERROR : " + e.getMessage());
        }
        return boards;
    }

    //게시물 전체 조회 ( 인기순 )
    public List<Board> BoardsPopularReadService( ) {
        List<Board> boards = null;
        try {
            boards = boardDao.BoardsReadPopularRepo();
        } catch (Exception e) {
            logger.error("[BoardService] BoardsReadService() ERROR : " + e.getMessage());
        }
        return boards;
    }

    //게시물 상세조회
    public Board BoardReadDetailService(String bsn) {
        Board board = null;
        try {
            board = boardDao.BoardReadDetailRepo(bsn);
        } catch (Exception e) {
            logger.error("[BoardService] BOardReadDetailService() ERROR : " + e.getMessage());
        }
        return board;
    }

    //게시물 생성 -> 게시물 생성에 대해서는 트랜젝션 처리를 어떻게 해줄것인가? ( s3에 파일업로드 후 게시물 업로드..)
    public String BoardCreateService(Board board, MultipartFile file) {

        String bsn = UUID.randomUUID().toString();
        String img_url = null;
        String img_name = null;
        PutObjectRequest putObjectRequest = null;
        try {
            img_name = BoardConst.s3_upload_folder_name + "/" + file.getOriginalFilename() + "." + apiHelper.makeTimeStamp();
            // book-img 디렉토리에 업로드
            putObjectRequest = new PutObjectRequest(bucket,  img_name, apiHelper.convertMultiPartToFile(file))
                                            .withCannedAcl(CannedAccessControlList.PublicRead);// 퍼블릭으로 공개하여 s3에 올림.
            amazonS3Client.putObject(putObjectRequest);
            img_url = amazonS3Client.getUrl(bucket,  img_name).toString();
        } catch (IOException e) {
            logger.error("[BoardService] createNewBoard() ERROR : " + "[Caused By] - AWS S3 Error " + e.getMessage());
            return "-1";
        }

        try {
            board.setBsn(bsn);
            board.setGroup_id(bsn);
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
    public String BoardUpdateService(Board board){

        board.setUpdated_time(apiHelper.makeNowTimeStamp());
        try {
            boardDao.BoardUpdateRepo(board);
            return board.getBsn();
        } catch (Exception e) {
            logger.error("[BoardService] updatedBoard() ERROR : " + e.getMessage());
        }
        return "-1";
    };

    //게시물 삭제 ( 게시물 삭제도 트랜잭션 처리 필요..)
    public String BoardDeleteService(String bsn) {
        //해당 bsn의 s3 이미지 삭제. ( 인자로 받은 bsn 값을 갖는 게시물의 img_name 필요 )
        String filename = null;
        try { // s3에 있는 이미지 file 삭제
            filename = boardDao.BoardImgNameRepo(bsn);

            if(filename == null) {
                logger.warn("[BoardService] deleteBoard, [CAUSE] = " + BoardConst.NO_IMG);
                return "-1";
            }
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filename));
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + filename + "] ");
            return "-1";
        }

        //게시물 삭제 오류 체크
        try {
            boardDao.BoardDeleteRepo(bsn);
            return bsn;
        } catch (Exception e) {
            logger.error("[BoardService] deleteBoard() ERROR : " + e.getMessage());
        }
        return "-1";
    };

    //좋아요 -> 트랜잭션 처리 필요
    @Transactional(rollbackFor = Exception.class)
    public String BoardCheckLikeService(String board_id, String liker_id, int like_cnt) {

        try { // like 테이블에 컬럼 추가
        boardDao.BoardCheckLikeRepo(board_id, liker_id);
        } catch (Exception e) {
        logger.error("[BoardService] BoardCheckLikes() ERROR : " + e.getMessage());
        return "-1";
     }

        try { // board 테이블의 like_cnt 증가
            boardDao.BoardIncreseLikeRepo(board_id, like_cnt);
            return board_id;
        } catch (Exception e) {
            logger.error("[BoardService] BoardIncreseLike() ERROR : " + e.getMessage());
        }
        return "-1";
    }

    //좋아요 취소
    @Transactional(rollbackFor = Exception.class)
    public String BoardCancelLikeService(String board_id, String liker_id, int like_cnt) {

        try { // like 테이블의 컬럼 삭제
            boardDao.BoardCancelLikeRepo(board_id, liker_id);
        } catch (Exception e ) {
            logger.error("[BoardService] BoardCancelLikes() ERROR : " + e.getMessage());
            return "-1";
        }

        try { // board 테이블의 like_cnt 감
            boardDao.BoardDecreseLikeRepo(board_id, like_cnt);
            return board_id;
        } catch (Exception e) {
            logger.error("[BoardService] BoardDecreseLike() ERROR : " + e.getMessage());
        }
        return "-1";
    }

    //좋아요 횟수 및 유저 리스트
    public List<String> BoardDetailLikeService(String bsn) {
        List<String> like_list = null;
        try {
            return boardDao.BoardDetailLikeRepo(bsn);
        } catch (Exception e) {
            logger.error("[BoardService] BoardDetailLike() ERROR : " + e.getMessage());
        }
        return like_list;
    }

    //게시물 신고하기
    public String BoardReportService(String bsn, int report_cnt) {

        try {
            boardDao.BoardReportRepo(bsn, report_cnt);
            return bsn;
        } catch (Exception e) {
            logger.error("[BoardService] BoardRepoert() ERROR : " + e.getMessage());
        }
        return "-1";
    }
    //5번 이상 신고된 게시물 삭제
    public Boolean BoardReportDelService(String bsn) {
        try {
            boardDao.BoardReportDelRepo(bsn);
            return true;
        } catch (Exception e) {
            logger.error("[BoardService] BoardReportDelete() ERROR : " + e.getMessage());
        }
        return false;
    }

    //댓글 달기
    @Transactional(rollbackFor = Exception.class)
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

    //댓글 조회 -> 게시물에 대한 댓글조회
    public List<Board> BoardCommentListService(String bsn) {
        List<Board> comment_list = null;
        try {
            comment_list = boardDao.BoardCommentsReadRepo(bsn);
        } catch (Exception e) {
            logger.error("[BoardService] ListComment() INSERT ERROR : " + e.getMessage());
        }
         return comment_list;
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
    public Boolean BoardCommentDeleteService(String bsn) {
        try {
            boardDao.BoardCommentDeleteRepo(bsn);
            return true;
        } catch (Exception e) {
            logger.error("[BoardService] deleteComment() : " + e.getMessage());
        }
        return false;
    }

    //service-code check
    public void filterBoardCode(int code) {
        validator.checkValidBoardServiceCode(code);
    }

    // s3에서 이미지 리스트 조회
    public Map<String, String> s3_file_list(String type) {

        Map<String, String> map = new HashMap<>();

        ObjectListing objectListing = amazonS3Client.listObjects(bucket);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {

            String[ ] folder = os.getKey().split("/");

            //길이 체크는 book-img/ 및 bg-im/를 제외하기 위해서.
            if(folder[0].equals(type) && folder.length == 2) {
                map.put(os.getKey(), amazonS3Client.getUrl(bucket, os.getKey()).toString());
            }
        }
        return map;
    }
}
