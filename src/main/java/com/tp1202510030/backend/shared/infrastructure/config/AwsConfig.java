package com.tp1202510030.backend.shared.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iot.IotClient;

@Configuration
public class AwsConfig {
    @Value("${aws.iot.region}")
    private String awsRegion;

    @Bean
    public IotClient iotClient() {
        return IotClient.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}
