package com.obaccelerator.common.error;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
public abstract class ObaBaseExceptionHandler {

    private static final String ERROR_PREFIX = "Error: ";

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<ObaErrorMessage> handleRuntimeException(Throwable e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_TECHNICAL_ERROR);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {ObaException.class})
    public ResponseEntity<ObaErrorMessage> handleObaException(ObaException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(e);
        return handle(errorMessage, e);
    }

    protected ResponseEntity<ObaErrorMessage> handle(ObaErrorMessage errorMessage, Throwable e) {
        log.error(ERROR_PREFIX, e);
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
    }
}
