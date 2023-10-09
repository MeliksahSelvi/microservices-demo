package com.melik.elastic.query.service.exception;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public class ElasticQueryServiceException extends RuntimeException{

    public ElasticQueryServiceException() {
        super();
    }

    public ElasticQueryServiceException(String message) {
        super(message);
    }

    public ElasticQueryServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
