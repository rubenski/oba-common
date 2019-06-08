package com.obaccelerator.common.error;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ObaErrorMessage {

    private List<CodeWithMessage> errors = new ArrayList<>();

    public ObaErrorMessage(ObaError... obaError) {
        for (ObaError error : obaError) {
            errors.add(new CodeWithMessage(error.getCode(), null,  error.getMessage()));
        }
    }

    public void addfFieldError(String field, String message) {
        errors.add(new CodeWithMessage(null, field, message));
    }

    public void addError(String code, String message) {
        errors.add(new CodeWithMessage(code, null, message));
    }


    public List<CodeWithMessage> getErrors() {
        return errors;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class CodeWithMessage {
        private String errorCode;
        private String field;
        private String message;
    }

}
