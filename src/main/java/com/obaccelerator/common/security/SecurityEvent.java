package com.obaccelerator.common.security;

public enum SecurityEvent {

    NO_AUTHORIZATION_HEADER_ON_INTERNAL_REQUEST("No authorization header on internal request"),
    ENDPOINT_NOT_DEFINED("Endpoint not defined"),
    INTERNAL_TOKEN_VALIDATION_PROBLEM("Internal token validation problem"),
    INTERNAL_ELEVATED_TOKEN_VALIDATION_PROBLEM("Internal elevated token validation problem");

    private String description;

    SecurityEvent(String description) {
        this.description = description;
    }

    public String getLogLine() {
        return "Security Event : " + this.description;
    }
}
