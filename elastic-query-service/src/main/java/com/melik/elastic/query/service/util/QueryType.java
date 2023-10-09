package com.melik.elastic.query.service.util;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public enum QueryType {

    KAFKA_STATE_STORE("KAFKA_STATE_STORE"),ANALYTICS_DATABASE("ANALYTICS_DATABASE");

    private final String type;

    QueryType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
