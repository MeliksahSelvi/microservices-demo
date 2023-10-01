package com.melik.elastic.query.service.config;

import com.melik.app.config.data.config.UserConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserConfigData userConfigData;

    public WebSecurityConfig(UserConfigData userConfigData) {
        this.userConfigData = userConfigData;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return null;

    }
}
