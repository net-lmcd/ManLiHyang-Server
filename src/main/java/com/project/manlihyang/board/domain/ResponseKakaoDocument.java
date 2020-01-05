package com.project.manlihyang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseKakaoDocument {
    List<String> authors; //도서 저자 리스트
    String contents; // 도서 소개
    String datetime; // 도서 출판날짜. ISO 8601. [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    String isbn; // 	국제 표준 도서번호(ISBN10 ISBN13) (ISBN10,ISBN13 중 하나 이상 존재하며, ' '(공백)을 구분자로 출력됩니다)
    int price; // 도서 정가
    String publisher; // 도서 출판사
    String status; // 도서 판매 상태 정보(정상, 품절, 절판 등), 상황에 따라 변동 가능성이 있으므로 문자열 처리 지양, 단순 노출요소로 활용을 권장합니다.
    String thumbnail; // 도서 표지 썸네일 URL
    String title; // 도서 제목
    List<String> transport; // 도서 번역자 리스트
    String url; // 도서 상세 url
}
