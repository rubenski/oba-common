package com.obaccelerator.common.form;

import lombok.*;

import java.util.Map;

@Getter
public class CheckBoxesField extends FieldDefinition {

    private final Map<String, CheckBoxValue> values;
    private final boolean required;

    public CheckBoxesField(String key, LabelExplanation labelExplanation, Map<String, CheckBoxValue> values, boolean required) {
        super(key, labelExplanation);
        this.values = values;
        this.required = required;
    }

    FieldType getType() {
        return FieldType.CHECKBOXES;
    }

    @Value
    public static class CheckBoxValue {
        String value;
        boolean selected;
    }
}
