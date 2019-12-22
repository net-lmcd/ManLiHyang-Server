package com.project.manlihyang.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Copyright 2019 NET-LMCD Corp. All rights Reserved.
 */

/*
 * Response 기본 객체
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseData<T> {
    /**
     * TIMESTAMP
     */
    @JsonProperty("timestamp")
    private long timeStamp;
    /**
     * SERVICE CODE - AppConst.[SERVICE_NAME]
     */
    @JsonProperty("service_code")
    private int serviceCode;
    /**
     * MESSAGE
     */
    @JsonProperty("message")
    private String message;
    /**
     * RESPONSE DATA
     */
    @JsonProperty("data")
    private T data;
}
