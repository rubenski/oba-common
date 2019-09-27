package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class TextInputField extends FieldDefinition {

    private LabelExplanation labelExplanation;
    private String regex;
    private int minLength;
    private int maxLength;
    private boolean required;

    public TextInputField(int minLength, int maxLength, boolean required) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
    }

    public TextInputField(String regex, int minLength, int maxLength, boolean required) {
        this.regex = regex;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
    }

    @Override
    public FieldType getType() {
        return FieldType.TEXT;
    }
}
