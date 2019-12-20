package com.project.manlihyang.user.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {

    @Select("SELECT EXISTS (SELECT email FROM user WHERE email=#{email})")
    boolean isExistsEmail(@Param("email") String email);

}
