package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
public class CheckBoxesField extends FieldDefinition {

    private final List<CheckBoxValue> values;
    private final boolean required;

    public CheckBoxesField(String key, LabelExplanation labelExplanation, List<CheckBoxValue> values, boolean required) {
        super(key, labelExplanation, FieldType.CHECKBOXES);
        this.values = values;
        this.required = required;
    }

    @Value
    public static class CheckBoxValue {
        String label;
        String value;
        boolean selected;
    }
}
