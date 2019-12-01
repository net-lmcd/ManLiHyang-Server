package com.project.manlihyang.board.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BoardRepository {

    @Select("SELECT NOW()")
    String getCurrentDateTime();
}
