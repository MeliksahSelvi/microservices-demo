package com.melik.kafka.admin.exception;

/**
 * @Author mselvi
 * @Created 26.09.2023
 */

public class KafkaClientException extends RuntimeException{

    public KafkaClientException() {
        super();
    }

    public KafkaClientException(String message) {
        super(message);
    }

    public KafkaClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
