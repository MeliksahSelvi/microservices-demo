package com.melik.elastic.query.web.client.common.exception;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

public class ElasticQueryWebClientException extends RuntimeException {
    public ElasticQueryWebClientException() {
        super();
    }

    public ElasticQueryWebClientException(String message) {
        super(message);
    }

    public ElasticQueryWebClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
