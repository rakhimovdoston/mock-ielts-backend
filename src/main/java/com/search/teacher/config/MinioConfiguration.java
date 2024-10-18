package com.search.teacher.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {
    private final ApplicationProperties applicationProperties;

    public MinioConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(applicationProperties.getMinio().getHost())
                .credentials(
                        applicationProperties.getMinio().getUsername(),
                        applicationProperties.getMinio().getPassword()
                ).build();
    }
}