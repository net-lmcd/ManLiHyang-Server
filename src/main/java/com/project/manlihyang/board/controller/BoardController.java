package com.project.manlihyang.board.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.project.manlihyang.BaseController;
import com.project.manlihyang.board.BoardConst;
import com.project.manlihyang.board.domain.Board;
import com.project.manlihyang.board.domain.ResponseKakaoBook;
import com.project.manlihyang.board.domain.LikeMeta;
import com.project.manlihyang.board.exception.BoardListSelectFailedException;
import com.project.manlihyang.board.exception.NoBoardCommentException;
import com.project.manlihyang.board.exception.NoBoardException;
import com.project.manlihyang.board.exception.NoBoardLikeException;
import com.project.manlihyang.board.service.BoardService;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("/board")
@CrossOrigin(origins = "*")
public class BoardController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private BoardService boardService;

    @Autowired
    AmazonS3 amazonS3Client;

    @Autowired
    RestTemplate restTemplate;

    @Autowired @Qualifier("kakao-book-header")
     private HttpHeaders kakaoBookHeader;

    //aws s3 bucket 이름
    @Value("${spring.aws.bucket}")
    private String bucket;

    //kakao book api url
    @Value("${kakao.openapi.book.url}")
    private String kakaoOpenApiBookUrl;

    //kakao book api rest api key
    @Value("${kakao.openapi.authorization}")
    private String kakaoOpenApiAuthorization;

    // Kaka book api 테스트
    @GetMapping("/Kakao/book")
    public ResponseEntity<ResponseKakaoBook> book_test (@RequestParam("book_name") String name,
                                                        @RequestParam("page") int page) {

        logger.info("book test !");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", kakaoOpenApiAuthorization);
        //kakaoBookHeader.add("Authorization", kakaoOpenApiAuthorization);
        String resourceBookUrl = kakaoOpenApiBookUrl + "?query=" + name + "&page=" + page;

        ResponseEntity<ResponseKakaoBook> response = restTemplate.exchange(resourceBookUrl, HttpMethod.GET, new HttpEntity(headers), ResponseKakaoBook.class);
        return response;
    }

    //배경 이미지 목록 ( default ) -> s3의 bg-img 폴더
    @GetMapping("/{service-code}/bg-img")
    public Map<String, String> bg_img_list(@PathVariable("service-code") int code) {

        logger.info("[GET] /board/bg-img" + "/" + code + "/" + "BoardBackgroundListAPI() ");
        return boardService.BoardBackGroundImgListService("bg-img");
    }

    // 책 이미지 목록 ( default ) -> s3의 book-img 폴더
    @GetMapping("/{service-code}/book-img")
    public Map<String, String> book_img_list(@PathVariable("service-code") int code) {
        logger.info("[GET] /board/bg-img" + "/" + code + "/" + "BoardBookListAPI() ");
        return boardService.BoardBookImgListService("book-img");
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
    public ResponseEntity<List<Board>> board_read(@PathVariable("service-code") int code) {
        logger.info("[GET] /board" + "/" + code + "/" + "ReadBoardsAPI()");

        boardService.filterBoardCode(code);
        List<Board> boardList = Optional.ofNullable(boardService.BoardsReadService()) // boardService.BoardsReadService가 null 이면 orElseThrow 인자가 호출됨
                .orElseThrow(BoardListSelectFailedException::new);
        return ResponseEntity.ok(boardList);
    }

    //특정 게시물 상세조회 ( 게시물 상세조회 할때 댓글도 조회 되야되는지 ?)
    @GetMapping("/{service-code}/{bsn}")
    public ResponseEntity<Board> board_read_detail(@PathVariable("service-code") int code,
                                      @PathVariable String bsn){
        logger.info("[GET] /board" + "/" + code + "/" + "ReadBoardDetailAPI() ");
        boardService.filterBoardCode(code);
        Board board = Optional.ofNullable(boardService.BoardReadDetailService(bsn))
                .orElseThrow(NoBoardException::new);
        return ResponseEntity.ok(board);
    }

    //게시물 생성 ( 게시물 이미지는 유저가 파일로 올려서 s3에 저장하고 배경이미지는 기존에 s3에 있던 이미지를 가져온다. )
    @PostMapping("/{service-code}") // 성공하면 bsn 리턴해줘야 해야됨....
    public ResponseEntity<?> board_insert(Board board, MultipartFile file,
                               @PathVariable("service-code") int code) {
        logger.info("[POST] /board" + "/" + code + "/" + "CreateBoardAPI() ");
        boardService.filterBoardCode(code);
        String bsn = boardService.BoardCreateService(board, file);
        return bsn != "-1"
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_CREATE_BOARD));
    }

    //게시물 수정 -> 이미지를 수정한다고 하면 기존 이미지 지우고 새로 업로드 필요.
    @PutMapping("/{service-code}")
    public ResponseEntity<?> board_update(Board board,
        @PathVariable("service-code") int code) {

            logger.info("[PUT] /board" + "/" + code + "/" + "UpdateBoardAPI() ");
            boardService.filterBoardCode(code);
            String bsn = boardService.BoardUpdateService(board);
            return bsn != "-1"
                    ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                    : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_UPDATE_BOARD));
    }

    //게시물 삭제
    @DeleteMapping("/{service-code}/{bsn}")
    public ResponseEntity<?> board_delete(@PathVariable("service-code") int code,
                                                           @PathVariable String bsn) {

        logger.info("[POST] /board" + "/" + code + "/" + "DeleteBoardAPI() ");
        boardService.filterBoardCode(code);
        String deletedBsn = boardService.BoardDeleteService(bsn);

        return deletedBsn != "-1"
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_DELETE_BOARD));
    }

    //게시물 좋아요 누르기  liker_id는 현제 세션의 기본키값
    // 유저가 해당 게시물을 눌른 상태면 취소되게, 안눌른 상태면 눌리게 해야됨 ( client에서 처리 )
    @PostMapping("/{service-code}/like/{board_id}/{liker_id}")
    public ResponseEntity<?> board_likes(@PathVariable("service-code") int code,
                           @PathVariable("board_id") String board_id,
                           @PathVariable("liker_id") String liker_id) {

        logger.info("[POST] /board/like" + "/" + code + "/" + "BoardCheckLikeAPI()");
        boardService.filterBoardCode(code);

        String bsn = boardService.BoardCheckLikeService(board_id, liker_id);
        return bsn != "-1"
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_BOARD_LIKE));
    }

    //게시물 좋아요 취소
    @DeleteMapping("/{service-code}/like/{board_id}/{liker_id}")
    public ResponseEntity<?> board_like_cancel(@PathVariable("service-code") int code,
                                 @PathVariable("board_id") String board_id,
                                 @PathVariable("liker_id") String liker_id) {
        logger.info("[DELETE] /board/like" + "/" + code + "/" + "BoardCancelLikeAPI()");
        boardService.filterBoardCode(code);
        String bsn = boardService.BoardCancelLikeService(board_id, liker_id);
        return bsn != "-1"
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_BOARD_CANCEL_LIKE));
    }

    //게시물 좋아요 횟수 및 누른 유저 확인
    @GetMapping("/{service-code}/like/{board_id}")
    public ResponseEntity<?> board_like_detail(@PathVariable("service-code") int code,
                                      @PathVariable("board_id") String board_id){

        logger.info("[GET] /board/like" + "/" + code + "/" + "BoardLikeDetailAPI()");
        boardService.filterBoardCode(code);

        List<String> likerList = Optional.ofNullable(boardService.BoardDetailLikeService(board_id))
                .orElseThrow(NoBoardLikeException::new);

        LikeMeta likeMeta = LikeMeta.builder()
                            .like_cnt(likerList.size())
                            .likers(likerList)
                            .build();
        return ResponseEntity.ok(likeMeta);
    }

    // 게시물 신고 하기
    @PutMapping("/{service-code}/report/{bsn}/{report_cnt}")
    public ResponseEntity<?> board_report(@PathVariable("service-code") int code,
                             @PathVariable("bsn") String bsn,
                             @PathVariable("report_cnt") int report_cnt) {

        logger.info("[PUT] /board/report" + "/" + code + "/" + "BoardReportAPI()");
        boardService.filterBoardCode(code);

        //해당 게시물 삭제 ( 5번 이상 신고된 게시물 삭제 )
        if(report_cnt == 5) {
            return boardService.BoardReportDelService(bsn) == true
                    ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                    : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_BOARD_REPORT_DELETE));
        }
        else {
            String report_bsn = boardService.BoardReportService(bsn, report_cnt);
            return bsn != "-1"
                    ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                    : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_BOARD_REPORT));
        }
    }

    //댓글 조회
    @GetMapping("/{service-code}/comment/{bsn}")
    public ResponseEntity<?> board_comment_read(@PathVariable("service-code") int code,
                                               @PathVariable("bsn") String bsn) {

        logger.info("[GET] /board/comment" + "/" + code + "/" + "BoardCommentReadAPI()");
        boardService.filterBoardCode(code);

        List<Board> comment_list = Optional.ofNullable(boardService.BoardCommentListService(bsn))
                                .orElseThrow(NoBoardCommentException::new);
        return ResponseEntity.ok(comment_list);
    }

    //댓글 삽입
    @PostMapping("/{service-code}/comment")
    public ResponseEntity<?> board_comment(@PathVariable("service-code") int code,
                              Board comment) {  // 뷰모의 group_seq, group_depth를 입력해줘야함. 결국, 댓글이 들어갈 자리는 부모 seq + 1, 부모 depth + 1 이다.

        logger.info("[POST] /board/comment" + "/" + code + "/" + "BoardCommentCreateAPI()");
        boardService.filterBoardCode(code);

        String bsn = boardService.BoardCommentService(comment);
        return bsn != "-1"
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_CREATE_COMMENT));
    }

    //댓글 수정
    @PutMapping("/{service-code}/comment")
    public ResponseEntity<?> board_comment_update(@PathVariable("service-code") int code,
                                                                     Board comment) {
        logger.info("[PUT] /board/comment" + "/" + code + "/" + "BoardCommentUpdateAPI()");
        boardService.filterBoardCode(code);
        String bsn = boardService.BoardCommentUpdateService(comment);
        return bsn != "-1"
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_UPDATE_COMMENT));
    }

    //댓글 삭제
    @PutMapping("/{service-code}/comment/{bsn}")
    public ResponseEntity<?> board_comment_delete(@PathVariable("service-code") int code,
                                                                     @PathVariable("bsn") String bsn) {
        logger.info("[PUT] /board/comment" + "/" + code + "/" + "BoardCommentDeleteAPI()");
        boardService.filterBoardCode(code);
        return boardService.BoardCommentDeleteService(bsn) == true
                ? ResponseEntity.status(200).body(successResponseBoard(bsn))
                : ResponseEntity.status(500).body(failedResponseBoard(BoardConst.FAILED_DELETE_COMMENT));
    }
}