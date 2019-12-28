package com.project.manlihyang.board.repository;

import com.project.manlihyang.board.domain.Board;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface BoardRepository {

    //기능별로 Repo 분리?? board, comment, likes

    //게시물 전체 조회 ( group_seq = 1 인 경우만 게시물이고 1보다 클 경우는 게시물의 댓글을 의미한다. )
    @Select("SELECT * from board where group_seq = 1")
    ArrayList<Board> BoardsReadRepo();

    //게시물 상세조회
    @Select("SELECT * from board where id = ${id}")
    Board BoardReadDetailRepo(int board_id);

    //게시물 생성
    @Insert("INSERT INTO board (writer_id, title, content, img_url, report_cnt, group_id, group_seq, group_depth, created_time, updated_time) VALUES(${writer_id}, #{title}, #{content}, #{img_url}, ${report_cnt}, ${group_id}, ${group_seq}, ${group_depth}, #{created_time}, #{updated_time})")
    @Options(useGeneratedKeys=true)
    int BoardCreateRepo(Board board);

    //게시물 수정
    @Update("UPDATE board set title=#{title}, content=#{content}, updated_time=#{updated_time} where id=${id}")
    int BoardUpdateRepo(Board board);

    // 게시물 삭제
    @Delete("DELETE FROM board where id = ${id}")
    int BoardDeleteRepo(int board_id);

    //게시물 좋아요 누름"
    @Insert("Insert likes (board_id, liker_id) VALUES(${board_id}, ${liker_id})")
    int BoardCheckLikeRepo(int board_id, int liker_id);
    //게시물 좋아요 취소
    @Delete("Delete from likes where board_id = ${board_id} && liker_id = ${liker_id}")
    int BoardCancelLikeRepo(int board_id, int liker_id);
    //게시물 좋아요 횟수 및 누른 사람 리스트
    @Select("Select liker_id from likes where board_id = ${board_id}")
    List<Integer> BoardDetailLikeRepo(int board_id);

    //게시물 신고하기
    @Update("Update board set report_cnt = ${report_cnt} + 1 where id = ${board_id}")
    int BoardReportRepo(int board_id, int report_cnt);

    //5번 이상 신고된 게시물 삭제
    @Delete("Delete from board where id = ${board_id}")
    int BoardReportDelRepo(int board_id);

    //게시물의 댓글 조회
    @Select("SELECT * FROM board where group_id = ${id} && group_id != id")
    Board ReadBoardCommentDetailRepo(int board_id);

    //게시물 댓글 달기 ( Client에서 해당 위치 바로위에 있는 group_seq + 1 및 depth + 1 을 넣어줘야함.
    // 댓글의 댓글이냐? 아니면 게시물의 댓글이냐를 구분 ( 댓글의 댓글일 경우 해당 댓글의 seq + 1, depth + 1 해주고 해당 seq보다 밑에 있는 애들 seq + 1 씩 증가 시켜 줘야 되고
    // 게시물의 댓글일 경우에는 가장 마지막에 추가된 seq + 1,  depth = 1 )
    //댓글을 쓰려면 update -> insert

    //댓글을 달기 위한 자리 마련 ( seq를 하나씩 뒤로 민다. )
    @Update("UPDATE board set group_seq = ${group_seq} + 1 where group_id = ${group_id} and group_seq > ${group_seq}")
    int UpdateBoardCommentGroupSeqRepo(Board comment);
    //댓글 달기
    @Insert("INSERT INTO board (writer_id, content, report_cnt, group_id, group_seq, group_depth, created_time, updated_time) VALUES(${writer_id}, #{content}, ${report_cnt}, ${group_id}, ${group_seq}, ${group_depth}, #{created_time}, #{updated_time})")
    int CreateBoardCommentRepo(Board comment);

    //게시물 댓글 수정

    //게시물 댓글 삭제
}
