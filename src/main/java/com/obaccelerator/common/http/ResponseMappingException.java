package com.obaccelerator.common.http;

public class ResponseMappingException extends RequestExecutionException {

    public ResponseMappingException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
