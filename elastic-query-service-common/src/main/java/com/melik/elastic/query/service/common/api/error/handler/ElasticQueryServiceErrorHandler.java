package com.melik.elastic.query.service.common.api.error.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@ControllerAdvice
public class ElasticQueryServiceErrorHandler {

    private final static Logger LOG = LoggerFactory.getLogger(ElasticQueryServiceErrorHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException e) {
        LOG.error("Acces Denied Exception", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this resource");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e) {
        LOG.error("Illegal Argument Exception", e);
        return ResponseEntity.badRequest().body("Illegal Argument Exception " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException e) {
        LOG.error("Service Runtime Exception", e);
        return ResponseEntity.badRequest().body("Service Runtime Exception " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        LOG.error("Internal Server Error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("A server error occured");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException e) {
        LOG.error("Method Argument Validation Exception", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->
                errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
