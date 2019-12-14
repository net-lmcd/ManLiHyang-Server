package com.project.manlihyang.board.repository;

import com.project.manlihyang.board.domain.Board;
import lombok.Generated;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface BoardRepository {

    @Select("SELECT * from board")
    ArrayList<Board> ReadBoards();

    @Insert("INSERT INTO board (writer_id, title, content, img_url, likes, report_cnt, group_id, group_seq, group_depth, created_time, updated_time) VALUES(${writer_id}, #{title}, #{content}, #{img_url}, ${likes}, ${report_cnt}, ${group_id}, ${group_seq}, ${group_depth}, #{created_time}, #{updated_time})")
    @Options(useGeneratedKeys=true)
    int CreateBoard(Board board);
}
