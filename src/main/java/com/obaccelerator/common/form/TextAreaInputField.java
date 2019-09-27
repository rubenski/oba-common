package com.obaccelerator.common.form;

import lombok.Value;

@Value
public class TextAreaInputField extends FieldDefinition {

    private LabelExplanation labelExplanation;
    private String regex;
    private int minLength;
    private int maxLength;
    private boolean required;

    @Override
    public FieldType getType() {
        return FieldType.TEXT_AREA;
    }
}
