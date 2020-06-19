package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public abstract class FieldDefinition {

    protected String value;
    protected String key;
    protected LabelExplanation labelExplanation;
    protected FieldType fieldType;

    public FieldDefinition(String key, LabelExplanation labelExplanation, FieldType fieldType) {
        this.key = key;
        this.labelExplanation = labelExplanation;
        this.fieldType = fieldType;
    }
}
