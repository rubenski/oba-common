package com.obaccelerator.common.http;

import lombok.Getter;

@Getter
public class UnexpectedHttpCodeException extends ResponseValidationException {

    private final int statusCode;

    public UnexpectedHttpCodeException(int statusCode) {
        super("Received unexpected HTTP code " + statusCode);
        this.statusCode = statusCode;
    }
}
