package com.project.manlihyang.user.service;

import com.project.manlihyang.common.RequestData;
import com.project.manlihyang.user.UserConst;
import com.project.manlihyang.user.domain.User;
import com.project.manlihyang.user.exception.NoEmailException;
import com.project.manlihyang.user.repository.UserRepository;
import com.project.manlihyang.util.Validator;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    private final UserRepository repo;
    private final Validator validator;

    public UserService(UserRepository repo, Validator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    /**
     * 존재하는 EMAIL 인지 검사
     * @param email email
     * @return bool
     */
    public boolean checkIsExistsEmail(String email) {
        boolean isExists = false;
        try {
            isExists = repo.isExistsEmail(email);
            if (isExists) {
                log.warn("[UserService] checkIsExistsEmail() WARN, [EMAIL] = " + email + ", [CAUSE] = " + UserConst.EMAIL_EXISTS);
            }
        } catch (Exception e) {
            log.error("[UserService] checkIsExistsEmail() ERROR : " + e.getMessage());
        }
        return isExists;
    }

    /**
     * 새로운 유저를 생성한다.
     * @param user user data
     * @return 1 : 0
     */
    public String createNewUser(User user) {
        String usn = createUserUsn();
        try {
            user.setUsn(usn);
            repo.insertNewUser(user);
            return usn;
        } catch (Exception e) {
            log.error("[UserService] createNewUser() ERROR : " + e.getMessage());
        }
        return "-1";
    }

    /**
     * usn으로 유저를 조회한다.
     * @param usn usn
     * @return USER
     */
    public User searchUser(String usn) {
        User user = null;
        try {
            user = repo.selectUserByUsn(usn);
            if (user == null) {
                log.warn("[UserService] searchUser() WARN, [USN] = " + usn + ", [CAUSE] = " + UserConst.NO_MEMBER);
            }
        } catch (Exception e) {
            log.error("[UserService] searchUser() ERROR : " + e.getMessage());
        }
        return user;
    }

    /**
     * service code 체크
     * @param code
     */
    public void filterCode(int code) {
        validator.checkValidUserServiceCode(code);
    }

    /**
     * Email, service code 체크
     * @param data
     */
    public void filterEmailAndCode(RequestData data) {
        validator.checkValidUserCode(data);
        Optional.ofNullable(data.getEmail())
                .orElseThrow(NoEmailException::new);
    }

    /**
     * USER 삭제
     * @param usn usn
     * @return true / false
     */
    public boolean removeUserByUsn(String usn) {
        try {
            if (repo.deleteUser(usn)) {
                return true;
            }
            else {
                log.warn("[UserService] removeUserByUsn() WARN, [USN] = " + usn + ", [CAUSE] = " + UserConst.NO_DELETE_USER_MATCHED);
                return false;
            }
        } catch (Exception e) {
            log.error("[UserService] removeUserByUsn() ERROR : " + e.getMessage());
            return false;
        }
    }

    /**
     * USER INFO UPDATE
     * @param user User
     * @return true / false
     */
    public boolean updateUserInfoByUsn(String usn, User user) {
        try {
            if (repo.updateUserInfo(usn, user)) {
                return true;
            } else {
                log.warn("[UserService] updateUserInfoByUsn() WARN, [USN] = " + usn + ", [CAUSE] = " + UserConst.NO_MEMBER);
            }
        } catch (Exception e) {
            log.error("[UserService] updateUserInfoByUsn() ERROR : " + e.getMessage());
        }
        return false;
    }
    /**
     * USER USN 생성
     * @return USN to String
     */
    private String createUserUsn() {
        return UUID.randomUUID().toString();
    }
}
