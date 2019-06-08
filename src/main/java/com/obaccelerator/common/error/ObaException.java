package com.obaccelerator.common.error;

import lombok.Getter;

@Getter
public class ObaException extends RuntimeException {
    private String code;
    private String message;
    private int httpStatus;

    public ObaException(ObaError obaError, Exception rootException) {
        super(rootException);
        this.code = obaError.getCode();
        this.message = obaError.getMessage();
        this.httpStatus = obaError.getHttpStatus();
    }

    public ObaException(ObaError obaError) {
        this.code = obaError.getCode();
        this.message = obaError.getMessage();
        this.httpStatus = obaError.getHttpStatus();
    }
}
