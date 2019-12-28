package com.project.manlihyang.board.controller;

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
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.annotations.Delete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/board")
@CrossOrigin(origins = "*")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

    @Autowired
    AmazonS3 amazonS3Client;

    @GetMapping("/img_download")
    public void download_test( ) {
        String bucketName = "manli-hang-s3";

        S3Object s3object = amazonS3Client.getObject(bucketName, "aaaa.txt");
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
            logger.info(String.valueOf(os.getSize()) );
        }
    }

    //s3에 이미지 넣기 테스트
    @PostMapping("/img")
    public void img_test(MultipartFile file) {

        String bucketName = "manli-hang-s3";

        /*  bucket 만들기
        if(s3client.doesBucketExistV2(bucketName)) {
            logger.info("Bucket name is not available."
                    + " Try again with a different Bucket name.");
        }  else {
            s3client.createBucket(bucketName);
        }
         */

        File file_upload = new File("/home/ktj/바탕화면/aaaa.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file_upload);
            fw.write("hello aws s3 with spring boot!!!");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // bucket에 파일 업로드 (방법 1 )
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, "file_upload_test.txt", file_upload);
        // 퍼블릭으로 공개하여 올림.
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3Client.putObject(putObjectRequest);

        // bucket에 파일 업로드 (방법2)
        /*
        amazonS3Client.putObject(
                bucketName,
                "aaaa.txt",
                file_upload
        );
*/
    }

    //게시물 조회 ( 전체 )
    @GetMapping("")
    public ArrayList<Board> board_read( ) {
        logger.info("게시물 조회 ( 전체 )");
        return boardService.BoardsReadService();
    }

    //특정 게시물 상세조회 ( 게시물 상세조회 할때 댓글도 조회 되야되는지 ?)
    @GetMapping("/{board_id}")
    public Board board_read_detail(@PathVariable int board_id){

        logger.info("특정 게시물 상세조회");
        return boardService.BoardReadDetailService(board_id);
    }

    //게시물 생성
    @PostMapping("")
    public int board_insert(Board board, MultipartFile file) {

        logger.info("게시물 생성");
        return boardService.BoardCreateService(board);
    }

    //게시물 수정
    @PutMapping("")
    public int board_update(Board board) {

        logger.info("게시물 수정");

        //현재 시간
        SimpleDateFormat time_format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String now = time_format.format(date);
        board.setUpdated_time(now);

        return boardService.BoardUpdateService(board);
    }

    //게시물 삭제
    @DeleteMapping("/{board_id}")
    public int board_delete(@PathVariable int board_id) {

        logger.info("게시물 삭제");
        return boardService.BoardDeleteService(board_id);
    }

    //게시물 좋아요 누르기  liker_id는 현제 세션의 기본키값
    // 유저가 해당 게시물을 눌른 상태면 취소되게, 안눌른 상태면 눌리게 해야됨 ( client에서 처리 )
    @PostMapping("/like/{board_id}/{liker_id}")
    public int board_likes(@PathVariable("board_id") int board_id,
                           @PathVariable("liker_id") int liker_id) {

        logger.info("게시물 좋아요");
        return boardService.BoardCheckLikeService(board_id, liker_id);
    }

    //게시물 좋아요 취소
    @DeleteMapping("/like/{board_id}/{liker_id}")
    public int board_like_cancel(@PathVariable("board_id") int board_id,
                                 @PathVariable("liker_id") int liker_id) {

        logger.info("게시물 좋아요 취소");
        return boardService.BoardCancelLikeService(board_id, liker_id);
    }

    //게시물 좋아요 횟수 및 누른 유저 확인
    @GetMapping("/like/{board_id}")
    public LikeMeta board_like_detail(@PathVariable("board_id") int board_id){

        logger.info("게시물 좋아요 누른 횟수 및 유저 확인");
        List<Integer> likerList = boardService.BoardDetailLikeService(board_id);
        LikeMeta likeMeta = LikeMeta.builder()
                            .like_cnt(likerList.size())
                            .likers(likerList)
                            .build();
        return likeMeta;
    }


    // 게시물 신고 하기
    @PutMapping("/report/{board_id}/{report_cnt}")
    public void board_report(@PathVariable("board_id") int board_id,
                            @PathVariable("report_cnt") int report_cnt) {

        logger.info("게시물 신고하기");

        //해당 게시물 삭제
        if(report_cnt == 5) boardService.BoardReportDelService(board_id);
        else boardService.BoardReportService(board_id, report_cnt);

    }

    //댓글 조회

    //댓글 삽입

    //댓글 삭제

    //댓글 수정
}
