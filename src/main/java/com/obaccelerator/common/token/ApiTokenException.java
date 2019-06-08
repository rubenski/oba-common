package com.obaccelerator.common.token;

public class ApiTokenException extends Exception {

    public ApiTokenException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApiTokenException(String s) {
        super(s);
    }
}
