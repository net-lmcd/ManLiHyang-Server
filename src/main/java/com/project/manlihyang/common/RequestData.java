package com.project.manlihyang.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotBlank;

/**
 * 기본 RequestData Format
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestData {
    @NotBlank
    @JsonProperty("serviceCode")
    private int serviceCode;
    @NotBlank
    @JsonProperty("usn")
    private long usn;
    @JsonProperty("email")
    private String email;
    @JsonProperty("nickname")
    private String nickname;
}

