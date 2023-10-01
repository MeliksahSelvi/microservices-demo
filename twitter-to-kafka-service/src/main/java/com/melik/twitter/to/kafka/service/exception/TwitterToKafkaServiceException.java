package com.melik.twitter.to.kafka.service.exception;

/**
 * @Author mselvi
 * @Created 25.09.2023
 */

public class TwitterToKafkaServiceException extends RuntimeException{

    public TwitterToKafkaServiceException() {
        super();
    }

    public TwitterToKafkaServiceException(String message) {
        super(message);
    }

    public TwitterToKafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
