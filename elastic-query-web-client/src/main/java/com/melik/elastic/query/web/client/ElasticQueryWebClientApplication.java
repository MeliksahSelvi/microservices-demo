package com.melik.elastic.query.web.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.melik")
public class ElasticQueryWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryWebClientApplication.class);
    }
}
