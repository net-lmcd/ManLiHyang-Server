package com.project.manlihyang.board.domain;

import lombok.*;
import java.util.List;

@Data
@Builder
public class LikeMeta {
    int like_cnt;
    List<String> likers;
}
