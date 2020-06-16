package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class DescriptionField extends FieldDefinition {

    private final String description;

    public DescriptionField(String key, LabelExplanation labelExplanation, String description) {
        super(key, labelExplanation);
        this.description = description;
    }

    FieldType getType() {
        return FieldType.DESCRIPTION;
    }
}
