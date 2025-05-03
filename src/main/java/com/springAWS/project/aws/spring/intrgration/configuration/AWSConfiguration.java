package com.springAWS.project.aws.spring.intrgration.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Slf4j
public class AWSConfiguration {

    @Value("${aws.access-key}")
    private String accessKey ;

    @Value("${aws.secret-key}")
    private String accessSecret ;

    @Value("${aws.region}")
    private String region;

    @Bean
    public S3Client createS3Client(){
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, accessSecret);
        log.info("***********");
        log.info(credentials.secretAccessKey());
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);
        Region region1 = Region.of(region);

        log.info("&&& "+region1.toString());
        return S3Client.builder()
                .region(region1)
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}
