package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
public class SelectInputField extends FieldDefinition {

    private final List<SelectListOption> options;
    private final boolean required;

    public SelectInputField(String key, LabelExplanation labelExplanation, List<SelectListOption> options, boolean required) {
        super(key, labelExplanation, FieldType.SELECT);
        this.options = options;
        this.required = required;
    }

    @Value
    public static class SelectListOption {
        String value;
        String label;
    }

    @Override
    void validate() {
        if (required) {
            if (values == null || values.isEmpty()) {
                fail();
            }
        }
    }
}
