package com.obaccelerator.common.form;

import lombok.Getter;
import java.util.List;

@Getter
public abstract class FieldDefinition {

    protected List<String> values;
    protected String key;
    protected LabelExplanation labelExplanation;
    protected FieldType type;

    public FieldDefinition(String key, LabelExplanation labelExplanation, FieldType type) {
        this.key = key;
        this.labelExplanation = labelExplanation;
        this.type = type;
    }

    abstract void validate();

    void fail() {
        throw new ApiRegistrationFormValidationException(key);
    }

    void setValue(List<String> values) {
        this.values = values;
        validate();
    }
}
