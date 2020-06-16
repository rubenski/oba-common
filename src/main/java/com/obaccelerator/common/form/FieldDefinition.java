package com.obaccelerator.common.form;


public abstract class FieldDefinition {

    protected String key;
    protected LabelExplanation labelExplanation;

    public FieldDefinition(String key, LabelExplanation labelExplanation) {
        this.key = key;
        this.labelExplanation = labelExplanation;
    }
}
