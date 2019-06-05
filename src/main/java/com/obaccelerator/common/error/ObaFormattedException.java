package com.obaccelerator.common.error;

import lombok.Getter;

@Getter
public class ObaFormattedException extends RuntimeException {
    private String code;
    private String message;
    private int httpStatus;

    public ObaFormattedException(ObaError obaError, Exception rootException) {
        super(rootException);
        this.code = obaError.getPrefix() + obaError.getNumber();
        this.message = obaError.getMessage();
        this.httpStatus = obaError.getHttpStatus();
    }

    public ObaFormattedException(ObaError obaError) {
        this.code = obaError.getPrefix() + obaError.getNumber();
        this.message = obaError.getMessage();
        this.httpStatus = obaError.getHttpStatus();
    }
}
