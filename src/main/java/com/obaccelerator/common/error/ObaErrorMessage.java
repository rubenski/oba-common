package com.obaccelerator.common.error;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.obaccelerator.common.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObaErrorMessage {

    private LocalDateTime utcTimeStamp = DateUtil.utcLocalDateTime();
    private List<FieldError> fieldErrors;
    private String errorCode;
    private String message;
    private int status;
    private String code;

    public ObaErrorMessage(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ObaErrorMessage(ObaError obaError) {
        this.status = obaError.getHttpStatus();
        this.code = obaError.getCode();
        this.message = obaError.getClientMessage();
    }

    public ObaErrorMessage(ObaException obaException) {
        this.status = obaException.getHttpStatus();
        this.code = obaException.getCode();
        this.message = obaException.getMessage();
    }

    public void addFieldErrors(Map<String, String> fieldErrors) {
        if(fieldErrors == null) {
            return;
        }
        this.fieldErrors = new ArrayList<>();
        for (String s : fieldErrors.keySet()) {
            this.fieldErrors.add(new FieldError(s, fieldErrors.get(s)));
        }
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class FieldError {
        private String field;
        private String message;
    }


}
