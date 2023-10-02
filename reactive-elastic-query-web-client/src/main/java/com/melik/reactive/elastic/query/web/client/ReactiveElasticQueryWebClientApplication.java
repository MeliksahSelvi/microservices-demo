package com.melik.reactive.elastic.query.web.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@SpringBootApplication
@ComponentScan(basePackages = "com.melik")
public class ReactiveElasticQueryWebClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveElasticQueryWebClientApplication.class);
    }
}
