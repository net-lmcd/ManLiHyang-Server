package com.project.manlihyang.util;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * [phantasmicmeans] created on 22/12/2019
 */
@Component
public class ApiHelper {
    public long makeTimeStamp() {
        return new Date().getTime();
    }
}
