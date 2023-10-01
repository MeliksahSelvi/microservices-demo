package com.melik.app.config.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "user-config")
public class UserConfigData {
    private String username;
    private String password;
    private String[] roles;
}
