package com.project.manlihyang.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Copyright 2019 NET-LMCD Corp. All rights Reserved.
 */

/*
 * Response 기본 객체
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> {
    /**
     * timestamp
     */
    @JsonProperty("timestamp")
    private long timeStamp;
    /**
     * service code - AppConst.[SERVICE_NAME]
     */
    @JsonProperty("serviceCode")
    private int serviceCode;
    /**
     * message
     */
    @JsonProperty("message")
    private String message;
    /**
     * response data
     */
    @JsonProperty("data")
    private T data;
}
