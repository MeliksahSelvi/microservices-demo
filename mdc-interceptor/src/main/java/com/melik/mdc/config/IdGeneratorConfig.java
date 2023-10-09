package com.melik.mdc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.IdGenerator;
import org.springframework.util.JdkIdGenerator;

/**
 * @Author mselvi
 * @Created 09.10.2023
 */

@Configuration
public class IdGeneratorConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new JdkIdGenerator();
    }
}
