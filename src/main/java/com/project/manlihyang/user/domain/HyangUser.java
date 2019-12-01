package com.project.manlihyang.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder @AllArgsConstructor
public class HyangUser {
    /**
     * id, auto increment by database
     */
    private int id;
    /**
     * USERNAME - 가입 닉네임
     */
    private String username;
    /**
     * EMAIL - 가입 이메일
     */
    private String email;
    /**
     * PASSWORD - 비밀번호
     */
    private String password;
    /**
     * CREATEDAT - 가입 시간
     */
    private String createdAt;
    /**
     * 알람 설정, 0 / 1
     */
    private boolean notice;
    /**
     * 채팅 알림 설정 여부, 0 / 1
     */
    private boolean noticeChat;
    /**
     * 프로필 이미지 URL
     */
    private String profileUrl;
    /**
     * 차단 유저 0 / 1
     */
    private boolean isBlocked;
    /**
     * 신고 누적 카운트
     */
    private int reportCnt;
}
