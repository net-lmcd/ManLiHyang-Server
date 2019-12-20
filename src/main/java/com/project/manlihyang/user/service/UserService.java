package com.project.manlihyang.user.service;

import com.project.manlihyang.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) {
        this.repo = repo;
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
        } catch (Exception e) {
            log.error("[UserService] checkIsExistsEmail() ERROR : " + e.getMessage());
        }
        return isExists;
    }
}
