package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class HiddenField extends FieldDefinition {

    private final String value;

    public HiddenField(String key, LabelExplanation labelExplanation, String value) {
        super(key, labelExplanation);
        this.value = value;
    }

    public FieldType getType() {
        return FieldType.HIDDEN;
    }
}
