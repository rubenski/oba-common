package com.obaccelerator.common.http;

public class ResponseMappingException extends RuntimeException {
    public ResponseMappingException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
