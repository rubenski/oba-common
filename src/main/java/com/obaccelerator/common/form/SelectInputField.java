package com.obaccelerator.common.form;

import lombok.Getter;

import java.util.Map;

@Getter
public class SelectInputField extends FieldDefinition {

    private final Map<String, String> options;
    private final boolean required;

    public SelectInputField(String key, LabelExplanation labelExplanation, Map<String, String> options, boolean required) {
        super(key, labelExplanation);
        this.options = options;
        this.required = required;
    }

    public FieldType getType() {
        return FieldType.SELECT_LIST;
    }
}
