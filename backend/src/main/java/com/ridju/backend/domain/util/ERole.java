package com.ridju.backend.domain.util;

public enum ERole {

    ADMIN("ADMIN"),
    STAFF("STAFF"),
    USER("USER");

    public final String label;

    ERole(String label) {
        this.label = label;
    }
}
