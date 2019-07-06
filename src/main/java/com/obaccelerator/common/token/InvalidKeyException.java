package com.obaccelerator.common.token;

public class InvalidKeyException extends RuntimeException {
    public InvalidKeyException(Throwable throwable) {
        super(throwable);
    }
}
