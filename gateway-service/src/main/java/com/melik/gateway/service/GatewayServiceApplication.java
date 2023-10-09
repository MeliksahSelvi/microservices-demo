package com.melik.gateway.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author mselvi
 * @Created 09.10.2023
 */

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = "com.melik")
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class);
    }
}
