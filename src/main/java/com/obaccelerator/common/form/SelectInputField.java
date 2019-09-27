package com.obaccelerator.common.form;

import lombok.Value;

import java.util.Map;

@Value
public class SelectInputField extends FieldDefinition {

    private LabelExplanation labelExplanation;
    private boolean required;
    private Map<String, String> options;

    @Override
    public FieldType getType() {
        return FieldType.SELECT_LIST;
    }
}
