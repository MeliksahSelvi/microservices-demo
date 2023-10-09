package com.melik.elastic.query.service.security;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

public enum PermissionType {

    READ("READ"), WRITE("WRITE"), ADMIN("ADMIN");

    private final String type;

    PermissionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
