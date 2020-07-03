package com.obaccelerator.common.security;

public class SecurityEventException extends RuntimeException {

    public SecurityEventException(SecurityEvent securityEvent, String message) {
        super(securityEvent + " : " + message);
    }
}
