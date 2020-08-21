package com.obaccelerator.common.error;

import lombok.Getter;

import java.util.List;

@Getter
public class ObaException extends RuntimeException {
    private String code;
    private String message;
    private int httpStatus;
    private String clientMessage;

    public ObaException(ObaError obaError, Exception rootException) {
        super(rootException);
        this.code = obaError.getCode();
        this.httpStatus = obaError.getHttpStatus();
        this.clientMessage = obaError.getClientMessage();
        this.message = obaError.getFormattedLogMessage();
    }

    public ObaException(ObaError obaError) {
        this.code = obaError.getCode();
        this.httpStatus = obaError.getHttpStatus();
        this.clientMessage = obaError.getClientMessage();
        this.message = obaError.getFormattedLogMessage();
    }

    public ObaException(ObaError obaError, List<String> params) {
        this.code = obaError.getCode();
        this.httpStatus = obaError.getHttpStatus();
        this.clientMessage = String.format(obaError.getClientMessage(), params);
        this.message = obaError.getFormattedLogMessage();
    }

}
