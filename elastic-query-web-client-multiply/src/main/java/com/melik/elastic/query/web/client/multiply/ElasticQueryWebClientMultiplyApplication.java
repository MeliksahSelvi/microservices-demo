package com.melik.elastic.query.web.client.multiply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.melik")
public class ElasticQueryWebClientMultiplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryWebClientMultiplyApplication.class);
    }
}
