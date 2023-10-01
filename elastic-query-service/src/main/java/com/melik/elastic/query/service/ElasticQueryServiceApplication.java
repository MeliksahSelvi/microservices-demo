package com.melik.elastic.query.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */


@SpringBootApplication
@ComponentScan(basePackages = "com.melik")
public class ElasticQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticQueryServiceApplication.class, args);
    }
}