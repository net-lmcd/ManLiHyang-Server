package com.project.manlihyang.user.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.sql.Timestamp;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User {
    /**
     * id, AUTO INCR
     */
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int id;
    /**
     * USER 식별자, randomly generated UUID 256-bit
     */
    @JsonProperty("usn")
    private String usn;
    /**
     * USERNAME
     */
    @NotEmpty(message = "이름을 확인해주세요.")
    @JsonProperty("username")
    private String username;
    /**
     * EMAIL (USER ID로 사용)
     */
    @NotEmpty(message = "이메일을 확인해주세요.")
    @Email
    @JsonProperty("email")
    private String email;
    /**
     * PASSWORD
     */
    @JsonProperty("password")
    @Size(min = 8, max = 24,message="8자이상 24자미만으로 작성해야 합니다.")
    private String password;
    /**
     * CREATED_AT TIMESTAMP
     */
    @JsonProperty("created_at")
    private Timestamp createdAt;
    /**
     * 글 추천 설정 여부
     */
    @JsonProperty("notice")
    private boolean notice;
    /**
     * 채팅 가능 여부
     */
    @JsonProperty("notice_chat")
    private boolean noticeChat;
    /**
     * 프로필 url
     */
    @JsonProperty("profile_url")
    private String profileUrl;
    /**
     * 블록된 사용자인지
     */
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    /**
     * 신고 당한 횟수
     */
    @JsonProperty("report_cnt")
    private int reportCnt;
}
