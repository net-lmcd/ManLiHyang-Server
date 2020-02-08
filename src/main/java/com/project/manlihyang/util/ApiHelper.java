package com.project.manlihyang.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * [phantasmicmeans] created on 22/12/2019
 */
@Slf4j
@Component
public class ApiHelper {
    private ObjectMapper mapper;
    public ApiHelper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    private static SimpleDateFormat time_format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

    public Long makeTimeStamp() {
        return new Date().getTime();
    }

    public String makeNowTimeStamp( ) {
        Date date = new Date();
        String now = time_format.format(date);
        return now;
    }

    public <T> void print(T data) {
        try {
            log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data));
        } catch(JsonProcessingException e) {
            log.error("[API HELPER] print() ERROR : " + e.getMessage());
        }
    }

    public <T> Object read (T data){
        try {
            return mapper.readValue((byte[]) data, Object.class);
        } catch (Exception e) {
            return new Object();
        }
    }

    public <T> Map<String, Object> convert(T data) {
        try {
            return mapper.convertValue(data, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            log.error("[API HELPER] convert() ERROR : " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
}
