package com.project.manlihyang;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ManlihyangConfiguration {

    /**
     * Basic ObjectMapper
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper messageObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    /**
     * Message Converter With Jackson
     * @param mapper ObjectMapper
     * @return converter
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper mapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        converter.setObjectMapper(mapper);
        return converter;
    }

    //Restemplate bean 설정
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    // Header bean 설정 ( json )
    @Bean(name = "kakao-book-header")
    public HttpHeaders KakaoBookHeader() {
        HttpHeaders headers = new HttpHeaders();
        return headers;
    }

    // Header bean 설정 ( json )
    @Bean(name = "application-json-header")
    public HttpHeaders applicationJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // Header bean 설정
    @Bean(name = "multipart-formdata-header")
    public HttpHeaders multipartFormdataHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }
}
