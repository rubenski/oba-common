package com.obaccelerator.common.http;

public class RequestExecutionException extends RuntimeException {

    public RequestExecutionException(Throwable throwable) {
        super(throwable);
    }

    public RequestExecutionException(String message) {
        super(message);
    }

    public RequestExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
