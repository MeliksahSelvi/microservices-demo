package com.melik.reactive.elastic.query.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity.csrf().disable()
                .authorizeExchange(exchange -> exchange
                        .anyExchange()
                        .authenticated());
        return serverHttpSecurity.build();
    }
}
