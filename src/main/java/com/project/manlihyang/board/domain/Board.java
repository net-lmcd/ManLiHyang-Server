package com.project.manlihyang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    int id; //게시물 기본키
    int writer_id; // 작성자 이름
    String title; // 게시물 제목
    String content; // 게시물 내용
    String img_url; // 이미지 경로
    int report_cnt; // 신고 갯수
    int group_id; // 게시물 그룹 id
    int group_seq; // 해당 그룹 내에서 순서
    int group_depth; //해당 그룹 내에서 depth
    String created_time; // 생성시간
    String updated_time; // 수정시간
}
