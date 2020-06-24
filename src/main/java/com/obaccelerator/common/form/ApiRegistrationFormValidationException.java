package com.obaccelerator.common.form;

public class ApiRegistrationFormValidationException extends RuntimeException {
    public ApiRegistrationFormValidationException(String key) {
        super("API registration form field with key " + key + " failed validation");
    }
}
