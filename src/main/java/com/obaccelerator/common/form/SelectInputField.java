package com.obaccelerator.common.form;

import lombok.Getter;

import java.util.List;

@Getter
public class SelectInputField extends FieldDefinition {

    private final List<Option> options;
    private final boolean required;

    public SelectInputField(String key, LabelExplanation labelExplanation, List<Option> options, boolean required) {
        super(key, labelExplanation);
        this.options = options;
        this.required = required;
    }

    private static class Option {
        private String value;
        private String label;
    }

    public FieldType getType() {
        return FieldType.SELECT_LIST;
    }
}
