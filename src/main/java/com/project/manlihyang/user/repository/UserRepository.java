package com.project.manlihyang.user.repository;

import com.project.manlihyang.user.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {

    /**
        EMAIL 중복 검사
     */
    @Select("SELECT EXISTS (SELECT email FROM user WHERE email=#{email})")
    boolean isExistsEmail(@Param("email") String email);

    /**
        USER 생성
     */
    @Insert("INSERT INTO user (usn, username, email, password, notice, notice_chat)  " +
            "VALUES (#{usn}, #{username}, #{email}, #{password}, #{notice}, #{noticeChat}) ")
    int insertNewUser(User user);

    /**
        USER 조회
     */
    @Select("SELECT * FROM user WHERE usn=#{usn}")
    User selectUserByUsn(@Param("usn") String usn);
}
