package com.project.manlihyang.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import lombok.*;

/**
 * 기본 RequestData Format
 */
@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RequestData {
    /**
     * SERVICE CODE - AppConst.[SERVICE_NAME]
     */
    @JsonProperty("service_code")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int serviceCode;
    /**
     * USER 식별자, randomly generated UUID
     */
    @JsonProperty("usn")
    private String usn;
    /**
     * EMAIL
     */
    @Email
    @JsonProperty("email")
    private String email;
    /**
     * NICKNAME
     */
    @JsonProperty("nickname")
    private String nickname;
}

