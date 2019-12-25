package com.project.manlihyang.user.repository;

import com.project.manlihyang.user.domain.User;
import org.apache.ibatis.annotations.*;
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

    /**
        USER 삭제
     */
    @Delete("DELETE FROM user WHERE usn=#{usn}")
    boolean deleteUser(@Param("usn") String usn);

    /**
        USER INFO 업데이트, 일괄 업뎃 적용
     */
    @Update("UPDATE USER " +
            "SET " +
            "username=#{user.username}, " +
            "email=#{user.email}, " +
            "notice=#{user.notice}, " +
            "notice_chat=#{user.noticeChat}, " +
            "profile_url=#{user.profileUrl}, " +
            "is_blocked=#{user.isBlocked}, " +
            "report_cnt=#{user.reportCnt} " +
            "WHERE usn=#{usn}")
    boolean updateUserInfo(@Param("usn") String usn, @Param("user") User user);
}
