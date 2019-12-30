package com.project.manlihyang.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
}
