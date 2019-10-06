package com.obaccelerator.common.error;


import com.obaccelerator.common.token.InvalidKeyException;
import com.obaccelerator.common.uuid.UuidInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class ObaBaseExceptionHandler {

    private static final String ERROR_PREFIX = "Error: ";

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<ObaErrorMessage> handleRuntimeException(Throwable e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_TECHNICAL_ERROR);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {DataAccessException.class})
    public ResponseEntity<ObaErrorMessage> handleDataAccessException(DataAccessException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_DB_EXCEPTION);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<ObaErrorMessage> handleSqlException(SQLException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_DB_EXCEPTION);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {HttpMessageConversionException.class})
    public ResponseEntity<ObaErrorMessage> handleHttpMessageNotReadableException(HttpMessageConversionException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_CLIENT_ERROR_INVALID_REQUEST);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {ServletRequestBindingException.class})
    public ResponseEntity<ObaErrorMessage> handleServletRequestBindingException(ServletRequestBindingException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_CLIENT_ERROR_INVALID_REQUEST);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ObaErrorMessage> handleEntityNotFoundException(EntityNotFoundException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_ENTITY_NOT_FOUND);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {UuidInvalidException.class})
    public ResponseEntity<ObaErrorMessage> handleUuidInvalidException(UuidInvalidException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_CLIENT_ERROR_INVALID_UUID_PROVIDED);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ObaErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(400, null, "Your message contained errors");
        errorMessage.addFieldErrors(collectBindingErrors(e));
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = InvalidKeyException.class)
    public ResponseEntity<ObaErrorMessage> handleInvalidKeyException(InvalidKeyException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError. APPLICATION_KEYS_INVALID_KEY);
        errorMessage.addFieldErrors(collectBindingErrors(e));
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<ObaErrorMessage> handleSqlException(SQLIntegrityConstraintViolationException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_DB_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = {ObaException.class})
    public ResponseEntity<ObaErrorMessage> handleObaException(ObaException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(e);
        return handle(errorMessage, e);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ObaErrorMessage> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, WebRequest webRequest) {
        ObaErrorMessage errorMessage = new ObaErrorMessage(ObaError.OBA_INVALID_CONTENT_TYPE);
        return handle(errorMessage, e);
    }

    protected ResponseEntity<ObaErrorMessage> handle(ObaErrorMessage errorMessage, Throwable e) {
        log.error(ERROR_PREFIX, e);
        return new ResponseEntity<>(errorMessage, HttpStatus.valueOf(errorMessage.getStatus()));
    }

    protected Map<String, String> collectBindingErrors(Throwable error) {
        Map<String, String> bindingErrors;
        if (error instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) error).getBindingResult();
            bindingErrors = new HashMap<>();

            bindingResult.getAllErrors().forEach(e -> {
                //SpringValidatorAdapter adapter = (SpringValidatorAdapter) e;
                FieldError fieldError = (FieldError) e;
                String field = fieldError.getField();
                String defaultMessage = e.getDefaultMessage();
                bindingErrors.put(field, defaultMessage);
            });
            return bindingErrors;
        }

        return null;
    }
}
