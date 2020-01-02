package com.project.manlihyang.board.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.domain.LikeMeta;
import com.project.manlihyang.board.service.BoardService;
import com.project.manlihyang.util.Const;
import jdk.nashorn.internal.objects.annotations.Getter;
import net.bytebuddy.build.Plugin;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/board")
@CrossOrigin(origins = "*")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

    @Autowired
    AmazonS3 amazonS3Client;

    @Value("${spring.aws.bucket}")
    private String bucket;

    //배경 이미지 목록
    @GetMapping("/{service-code}/bg-img")
    public Map<String, String> bg_img_list(@PathVariable("service-code") int code) {

        logger.info("[GET] /board/bg-img" + "/" + code + "/" + "BoardBackgroundListAPI() ");
        return boardService.BoardBackGroundImgListService();
    }

    @GetMapping("/img_download")
    public void download_test( ) {

        S3Object s3object = amazonS3Client.getObject(bucket, "aaaa.txt");
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File("/home/ktj/바탕화면/bbbb.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/img_test")
    public void tttest( ) {

        // bucket 리스트 출력
        List<Bucket> buckets = amazonS3Client.listBuckets();
        for(Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }

        String bucketName = "manli-hang-s3";
        //bucket 안에 있는 파일리스트
        ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
        for(S3ObjectSummary os : objectListing.getObjectSummaries()) {
            logger.info(os.getKey());
            logger.info(String.valueOf(os.getSize()));
            logger.info(amazonS3Client.getUrl(bucket, os.getKey()).toString());
        }
    }

    //게시물 조회 ( 전체 )
    @GetMapping("/{service-code}")
    public ArrayList<Board> board_read(@PathVariable("service-code") int code) {
        logger.info("[GET] /board" + "/" + code + "/" + "ReadBoardsAPI()");

        boardService.filterCode(code);
        return boardService.BoardsReadService();
    }

    //특정 게시물 상세조회 ( 게시물 상세조회 할때 댓글도 조회 되야되는지 ?)
    @GetMapping("/{service-code}/{bsn}")
    public Board board_read_detail(@PathVariable("service-code") int code,
                                   @PathVariable String bsn){
        logger.info("[GET] /board" + "/" + code + "/" + "ReadBoardDetailAPI() ");
        boardService.filterCode(code);
        return boardService.BoardReadDetailService(bsn);
    }

    //게시물 생성 ( 게시물 이미지는 유저가 파일로 올려서 s3에 저장하고 배경이미지는 기존에 s3에 있던 이미지를 가져온다. )
    @PostMapping("/{service-code}")
    public String board_insert(Board board, MultipartFile file,
                               @PathVariable("service-code") int code) {
        logger.info("[POST] /board" + "/" + code + "/" + "CreateBoardAPI() ");
        boardService.filterCode(code);
        return boardService.BoardCreateService(board, file);
    }

    //게시물 수정 -> 이미지를 수정한다고 하면 기존 이미지 지우고 새로 업로드 필요.
    @PutMapping("/{service-code}")
    public int board_update(Board board,
                            @PathVariable("service-code") int code) {

        logger.info("[PUT] /board" + "/" + code + "/" + "UpdateBoardAPI() ");
        boardService.filterCode(code);
        return boardService.BoardUpdateService(board);
    }

    //게시물 삭제
    @DeleteMapping("/{service-code}/{bsn}")
    public int board_delete(@PathVariable("service-code") int code,
                            @PathVariable String bsn) {

        logger.info("[POST] /board" + "/" + code + "/" + "DeleteBoardAPI() ");
        boardService.filterCode(code);
        return boardService.BoardDeleteService(bsn);
    }

    //게시물 좋아요 누르기  liker_id는 현제 세션의 기본키값
    // 유저가 해당 게시물을 눌른 상태면 취소되게, 안눌른 상태면 눌리게 해야됨 ( client에서 처리 )
    @PostMapping("/{service-code}/like/{board_id}/{liker_id}")
    public int board_likes(@PathVariable("service-code") int code,
                           @PathVariable("board_id") String board_id,
                           @PathVariable("liker_id") String liker_id) {

        logger.info("[POST] /board/like" + "/" + code + "/" + "BoardCheckLikeAPI()");
        boardService.filterCode(code);
        return boardService.BoardCheckLikeService(board_id, liker_id);
    }

    //게시물 좋아요 취소
    @DeleteMapping("/{service-code}/like/{board_id}/{liker_id}")
    public int board_like_cancel(@PathVariable("service-code") int code,
                                 @PathVariable("board_id") String board_id,
                                 @PathVariable("liker_id") String liker_id) {
        logger.info("[DELETE] /board/like" + "/" + code + "/" + "BoardCancelLikeAPI()");
        boardService.filterCode(code);
        return boardService.BoardCancelLikeService(board_id, liker_id);
    }

    //게시물 좋아요 횟수 및 누른 유저 확인
    @GetMapping("/{service-code}/like/{board_id}")
    public LikeMeta board_like_detail(@PathVariable("service-code") int code,
                                      @PathVariable("board_id") String board_id){

        logger.info("[GET] /board/like" + "/" + code + "/" + "BoardLikeDetailAPI()");
        boardService.filterCode(code);

        List<String> likerList = boardService.BoardDetailLikeService(board_id);
        LikeMeta likeMeta = LikeMeta.builder()
                            .like_cnt(likerList.size())
                            .likers(likerList)
                            .build();
        return likeMeta;
    }

    // 게시물 신고 하기
    @PutMapping("/{service-code}/report/{bsn}/{report_cnt}")
    public void board_report(@PathVariable("service-code") int code,
                             @PathVariable("bsn") String bsn,
                             @PathVariable("report_cnt") int report_cnt) {

        logger.info("[PUT] /board/report" + "/" + code + "/" + "BoardReportAPI()");
        boardService.filterCode(code);

        //해당 게시물 삭제 ( 5번 이상 신고된 게시물 삭제 )
        if(report_cnt == 5) boardService.BoardReportDelService(bsn);
        else boardService.BoardReportService(bsn, report_cnt);
    }

    //댓글 조회
    @GetMapping("/{service-code}/comment/{bsn}")
    public ArrayList<Board> board_comment_read(@PathVariable("service-code") int code,
                                               @PathVariable("bsn") String bsn) {

        logger.info("[GET] /board/comment" + "/" + code + "/" + "BoardCommentReadAPI()");
        boardService.filterCode(code);
        return boardService.BoardCommentListService(bsn);
    }

    //댓글 삽입
    @PostMapping("/{service-code}/comment")
    public String board_comment(@PathVariable("service-code") int code,
                              Board comment) {  // 뷰모의 group_seq, group_depth를 입력해줘야함. 결국, 댓글이 들어갈 자리는 부모 seq + 1, 부모 depth + 1 이다.

        logger.info("[POST] /board/comment" + "/" + code + "/" + "BoardCommentCreateAPI()");
        boardService.filterCode(code);
        return boardService.BoardCommentService(comment);
    }

    //댓글 수정
    @PutMapping("/{service-code}/comment")
    public String board_comment_update(@PathVariable("service-code") int code,
                                       Board comment) {
        logger.info("[PUT] /board/comment" + "/" + code + "/" + "BoardCommentUpdateAPI()");
        boardService.filterCode(code);
        return boardService.BoardCommentUpdateService(comment);
    }

    //댓글 삭제
    @PutMapping("/{service-code}/comment/{bsn}")
    public String board_comment_delete(@PathVariable("service-code") int code,
                                       @PathVariable("bsn") String bsn) {
        logger.info("[PUT] /board/comment" + "/" + code + "/" + "BoardCommentDeleteAPI()");
        boardService.filterCode(code);
        return boardService.BoardCommentDeleteService(bsn);
    }
}