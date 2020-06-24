package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class TextAreaInputField extends FieldDefinition {

    private final String regex;
    private final int minLength;
    private final int maxLength;
    private final boolean required;

    public TextAreaInputField(String key, LabelExplanation labelExplanation, String regex, int minLength, int maxLength, boolean required) {
        super(key, labelExplanation, FieldType.TEXT_AREA);
        this.regex = regex;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
    }

    @Override
    void validate() {
        if(!values.isEmpty() && values.get(0).length() < minLength) {
            throw new ApiRegistrationFormValidationException(key);
        }
    }
}
