package com.project.manlihyang.user.controller;

import com.project.manlihyang.BaseController;
import com.project.manlihyang.common.RequestData;
import com.project.manlihyang.user.domain.User;
import com.project.manlihyang.user.exception.NoEmailException;
import com.project.manlihyang.user.service.UserService;
import com.project.manlihyang.util.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class UserController extends BaseController {
    private Validator validator;
    private UserService user;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        return null;
    }

    /**
     * email 중복 검사
     * @param request
     * @return
     */
    @PostMapping("/users/confirm")
    public ResponseEntity<?> confirmIsValidEmail(@RequestBody RequestData request) {
        Optional.ofNullable(request.getEmail())
                .orElseThrow(NoEmailException::new);

        boolean isExists = user.checkIsExistsEmail(request.getEmail()); // 존재하는 email 인지 체크

        if (isExists) {
            return ResponseEntity.ok(successResponseU());
        }
        // TODO Failed Response 추가
        return ResponseEntity.status(409).build();
    }

}


/**
 유저 가입 POST /users
 유저 탈퇴 DELETE /users/{userid}
 유저 정보 수정 PUT /users/{userid}
 유저 비밀번호 변경 PUT /users/password
 아이디 찾기 GET /users/{userid}/id
 비밀번호 찾기 GET /users/{userid}/password (바로 변경 or 랜덤 생성 비번)
 아이디 및 비밀번호 본인인증..
 아이디 중복 검사 GET /users/{userid}/confirm
 유저 정보 조회 GET /users/{userid}유저 가입 POST /users
 유저 탈퇴 DELETE /users/{userid}
 유저 정보 수정 PUT /users/{userid}
 유저 비밀번호 변경 PUT /users/password
 아이디 찾기 GET /users/{userid}/id
 비밀번호 찾기 GET /users/{userid}/password (바로 변경 or 랜덤 생성 비번)
 아이디 및 비밀번호 본인인증..
 아이디 중복 검사 GET /users/{userid}/confirm
 유저 정보 조회 GET /users/{userid}
 */