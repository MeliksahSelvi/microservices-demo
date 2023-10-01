package com.melik.common.config;

import com.melik.app.config.data.config.RetryConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Author mselvi
 * @Created 26.09.2023
 */

/*
* Kafka Admin'deki retry ile yapmak istediğimiz yerlerdeki gibi yeniden deneme işlemlerini bu Configuration ile yaparız.
* */
@Configuration
public class RetryConfig {

    private final RetryConfigData configData;

    public RetryConfig(RetryConfigData configData) {
        this.configData = configData;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate=new RetryTemplate();

        /*
        * her denemede bekleme süresini arttırır.
        * */
        ExponentialBackOffPolicy exponentialBackOffPolicy=new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(configData.getInitialIntervalMs());
        exponentialBackOffPolicy.setMaxInterval(configData.getMaxIntervalMs());
        exponentialBackOffPolicy.setMultiplier(configData.getMultiplier());

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        SimpleRetryPolicy simpleRetryPolicy=new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(configData.getMaxAttempts());

        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }
}
