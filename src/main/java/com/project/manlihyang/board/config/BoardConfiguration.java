package com.project.manlihyang.board.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardConfiguration {

    /*
    @Value("${spring.aws.access-key}")
    private String AccessKey;

    @Value("${spring.aws.secret-key}")
    private String SecretKey;

    @Value("${spring.aws.region}")
    private String region;

    @Value("${spring.aws.bucket}")
    private String bucket;
    */

    // aws s3 연동을 위한 bean 설
    @Bean
    public AmazonS3 amazonS3Client( ) {

        AWSCredentials credentials = new BasicAWSCredentials("", "");
        AmazonS3 s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_2) // 서울 region
                .build();

        return s3client;
    }
}
