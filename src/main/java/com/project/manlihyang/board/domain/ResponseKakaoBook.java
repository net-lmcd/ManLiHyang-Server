package com.project.manlihyang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
 response Format
  "documents": [
        {
            "authors": [
                "이일민"
            ],
            "contents": "『토비의 스프링 3.1 세트』는 스프링을 처음 접하거나 스프링을 경험했지만 스프링이 어렵게 느껴지는 개발자부터 스프링을 활용한 아키텍처를 설계하고 프레임워크를 개발하려고 하는 아키텍트에 이르기까지 모두 참고할 수 있는 스프링 바이블이다. 단순한 예제를 스프링 3.0과 스프링 3.1의 기술을 적용하며 발전시켜 나가는 과정을 통해 스프링의 핵심 프로그래밍 모델인 IoC/DI, PSA, AOP의 원리와 이에 적용된 다양한 디자인 패턴, 프로그래밍 기법을",
            "datetime": "2012-09-21T00:00:00.000+09:00",
            "isbn": "8960773433 9788960773431",
            "price": 75000,
            "publisher": "에이콘출판",
            "sale_price": 67500,
            "status": "정상판매",
            "thumbnail": "https://search1.kakaocdn.net/thumb/R120x174.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F838115%3Ftimestamp%3D20200105122651",
            "title": "토비의 스프링 3.1 세트(에이콘 오픈소스 프로그래밍 시리즈)(전2권)",
            "translators": [],
            "url": "https://search.daum.net/search?w=bookpage&bookId=838115&q=%ED%86%A0%EB%B9%84%EC%9D%98+%EC%8A%A4%ED%94%84%EB%A7%81+3.1+%EC%84%B8%ED%8A%B8%28%EC%97%90%EC%9D%B4%EC%BD%98+%EC%98%A4%ED%94%88%EC%86%8C%EC%8A%A4+%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D+%EC%8B%9C%EB%A6%AC%EC%A6%88%29%28%EC%A0%842%EA%B6%8C%29"
        },
        {
        ...}
        ..
        ],
        meta": {
        "is_end": false,
        "pageable_count": 211,
        "total_count": 215
    }

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseKakaoBook {
    ResponseKakaoMeta meta;
    List<ResponseKakaoDocument> documents;
}
