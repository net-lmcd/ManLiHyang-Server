package com.project.manlihyang.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * [phantasmicmeans] created on 22/12/2019
 * 공통 및 유틸성 메소드 모음
 */
@Slf4j
@Component
@AllArgsConstructor
public class LogHelper {
    private ObjectMapper mapper;

    public <T> void printWithObjMapper(T data) {
        try {
            log.info("\n[PRINT DATA] : " + mapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.error("[ApiHelper] printWithObjMapper() ERROR : " + e.getMessage());
        }
    }

    public <T> void printPrettyWithObjMapper(T data) {
        try {
            String value = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            log.info("\n[PRINT DATA] : " + value);
        } catch (JsonProcessingException e) {
            log.error("[ApiHelper] printPrettyWithObjMapper() ERROR : " + e.getMessage());
        }
    }
}
