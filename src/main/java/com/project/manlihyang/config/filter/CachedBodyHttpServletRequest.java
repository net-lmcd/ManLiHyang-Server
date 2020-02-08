package com.project.manlihyang.config.filter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private static ObjectMapper mapper;
    private byte[] cachedBody;
    private String method;
    private String path;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.method = request.getMethod();
        this.path = request.getServletPath();
        this.mapper = new ObjectMapper();
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }

    public void print() {
        StringBuilder builder = new StringBuilder();
        builder.append("[" + method + "] " + path);

        try {
            if (this.cachedBody.length != 0) {
                builder.append(", ");
                builder.append(convert(read(this.cachedBody, Object.class)));
            }
        } catch (Exception e) {
            log.error("[REQUEST BODY] Mapping ERROR : " + e.getMessage());
        }
        log.info(builder.toString());
    }

    private <T, K> K read(T data, Class<K> type) throws IOException {
        return mapper.readValue((byte []) data, type);
    }

    private <T> String convert(T data) throws JsonProcessingException {
        return mapper.writeValueAsString(data);
    }
}
