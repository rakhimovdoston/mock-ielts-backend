package com.search.teacher.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Configuration
public class ApplicationProperties {

    private final Minio minio = new Minio();

    @Setter
    @Getter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Minio {
        String applicationName;
        String host;
        String username;
        String password;
    }

}
