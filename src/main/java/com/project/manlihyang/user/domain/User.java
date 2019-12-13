package com.project.manlihyang.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter @Getter @Builder @AllArgsConstructor
public class User {
    /**
     * id, AUTO INCR
     */
    @JsonProperty("id")
    private int id;
    /**
     * USERNAME
     */
    @JsonProperty("username")
    private String username;
    /**
     * EMAIL (USER ID로 사용)
     */
    @JsonProperty("email")
    private String email;
    /**
     * PASSWORD
     */
    @JsonProperty("password")
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
