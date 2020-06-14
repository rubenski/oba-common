package com.obaccelerator.common.form;

import lombok.Value;

import java.util.Map;

@Value
public class SelectInputField extends FieldDefinition {

    LabelExplanation labelExplanation;
    boolean required;
    Map<String, String> options;

    @Override
    public FieldType getType() {
        return FieldType.SELECT_LIST;
    }
}
