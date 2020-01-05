package com.project.manlihyang.board.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.manlihyang.board.controller.BoardController;
import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.BoardConst;
import com.project.manlihyang.board.repository.BoardRepository;
import com.project.manlihyang.util.ApiHelper;
import com.project.manlihyang.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    //게시물 조회
    public List<Board> BoardsReadService( ) {
        List<Board> boards = null;
        try {
            boards = boardDao.BoardsReadRepo();
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

    //게시물 생성
    public String BoardCreateService(Board board, MultipartFile file) {

        String bsn = UUID.randomUUID().toString();
        String img_url = null;
        String img_name = null;
        PutObjectRequest putObjectRequest = null;
        try {
            img_name = file.getOriginalFilename() + "." + apiHelper.makeTimeStamp();
            // book-img 디렉토리에 업로드
            putObjectRequest = new PutObjectRequest(bucket, BoardConst.s3_book_folder_name + "/" + img_name, apiHelper.convertMultiPartToFile(file))
                                            .withCannedAcl(CannedAccessControlList.PublicRead);// 퍼블릭으로 공개하여 s3에 올림.
            amazonS3Client.putObject(putObjectRequest);
            img_url = amazonS3Client.getUrl(bucket, BoardConst.s3_book_folder_name + "/" + img_name).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            /*
            board.builder()
                    .bsn(bsn) // bsn 세팅
                    .group_id(bsn) // 게시물 원본일 경우 group_ip는 자기 자신
                    .created_time(apiHelper.makeNowTimeStamp())
                    .updated_time(apiHelper.makeNowTimeStamp())
                    .img_url(img_url)
                    .img_name(img_name)
                    .build();
             */
            board.setBsn(bsn);
            board.setGroup_id(bsn);
            board.setCreated_time(apiHelper.makeNowTimeStamp());
            board.setUpdated_time(apiHelper.makeNowTimeStamp());
            board.setImg_url(img_url);
            board.setImg_name(img_name);

            logger.info("Board : " + board.toString());
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
    //게시물 삭제
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
    public List<String> BoardDetailLikeService(String bsn) { return boardDao.BoardDetailLikeRepo(bsn);}

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

    //service-code check
    public void filterCode(int code) {
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
