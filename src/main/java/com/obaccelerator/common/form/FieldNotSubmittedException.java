package com.obaccelerator.common.form;

public class FieldNotSubmittedException extends RuntimeException {

    public FieldNotSubmittedException(String key) {
        super("Field with key " + key + " was missing in submitted form");
    }
}
