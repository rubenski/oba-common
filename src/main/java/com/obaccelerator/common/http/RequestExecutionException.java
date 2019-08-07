package com.obaccelerator.common.http;

public class RequestExecutionException extends RuntimeException {

    public RequestExecutionException(Throwable throwable) {
        super(throwable);
    }
}
