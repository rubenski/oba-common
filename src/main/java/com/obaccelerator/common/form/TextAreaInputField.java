package com.obaccelerator.common.form;

import lombok.Value;

@Value
public class TextAreaInputField extends FieldDefinition {

    LabelExplanation labelExplanation;
    String regex;
    int minLength;
    int maxLength;
    boolean required;



    @Override
    public FieldType getType() {
        return FieldType.TEXT_AREA;
    }
}
