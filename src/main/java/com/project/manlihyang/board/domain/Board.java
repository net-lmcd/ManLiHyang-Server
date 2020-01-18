package com.project.manlihyang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    int id; //게시물 기본키
    String bsn; // 게시물 식별자
    String writer_id; // 작성자 이름
    String title; // 게시물 제목
    int like_cnt; // 게시물 좋아요 갯수
    String content; // 게시물 내용
    String img_url; // 이미지 경로
    String img_name; // s3에 저장될 이미지 이름
    String bg_img_url; // 배경 이미지 url
    int report_cnt; // 신고 갯수
    char is_del; // 댓글이 삭제된 댓글인지 판별하는 변수
    String group_id; // 게시물 그룹 id
    int group_seq; // 해당 그룹 내에서 순서
    int group_depth; //해당 그룹 내에서 depth
    String created_time; // 생성시간
    String updated_time; // 수정시간
}
