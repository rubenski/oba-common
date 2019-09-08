package com.obaccelerator.common.http;

import org.apache.http.HttpResponse;

public class ResponseMappingException extends RuntimeException {

    public ResponseMappingException(String s, Throwable throwable) {
        super(s, throwable);
    }

}
