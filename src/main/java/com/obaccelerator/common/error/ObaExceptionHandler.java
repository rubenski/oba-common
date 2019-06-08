package com.obaccelerator.common.error;


import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@ControllerAdvice
public class ObaExceptionHandler {


    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ObaErrorMessage> handleRuntimeException(RuntimeException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_TECHNICAL_ERROR);
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
    }

    @ExceptionHandler(value = {DataAccessException.class})
    public ResponseEntity<ObaErrorMessage> handleDataAccessException(DataAccessException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_DATA_ACCESS_EXCEPTION);
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
    }

    @ExceptionHandler(value = {ObaException.class})
    public ResponseEntity<ObaErrorMessage> handleObaException(ObaException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(e);
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
    }
}
