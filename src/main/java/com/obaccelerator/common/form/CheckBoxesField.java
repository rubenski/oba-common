package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
public class CheckBoxesField extends FieldDefinition {

    private final List<CheckBoxValue> values;
    private final boolean required;

    public CheckBoxesField(String key, LabelExplanation labelExplanation, List<CheckBoxValue> values, boolean required) {
        super(key, labelExplanation);
        this.values = values;
        this.required = required;
    }

    FieldType getType() {
        return FieldType.CHECKBOXES;
    }

    @Value
    public static class CheckBoxValue {
        String label;
        String value;
        boolean selected;
    }
}
