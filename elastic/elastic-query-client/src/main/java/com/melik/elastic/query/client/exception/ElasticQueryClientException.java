package com.melik.elastic.query.client.exception;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

public class ElasticQueryClientException extends RuntimeException{

    public ElasticQueryClientException() {
        super();
    }

    public ElasticQueryClientException(String message) {
        super(message);
    }

    public ElasticQueryClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
