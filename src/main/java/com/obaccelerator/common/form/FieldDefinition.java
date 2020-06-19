package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public abstract class FieldDefinition {

    protected String value;
    protected String key;
    protected LabelExplanation labelExplanation;
    protected FieldType type;

    public FieldDefinition(String key, LabelExplanation labelExplanation, FieldType type) {
        this.key = key;
        this.labelExplanation = labelExplanation;
        this.type = type;
    }
}
