package com.project.manlihyang.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [phantasmicmeans] created on 22/12/2019
 */
@Component
public class ApiHelper {

    private static SimpleDateFormat time_format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

    public Long makeTimeStamp() {
        return new Date().getTime();
    }

    public String makeNowTimeStamp( ) {
        Date date = new Date();
        String now = time_format.format(date);
        return now;
    }
}
