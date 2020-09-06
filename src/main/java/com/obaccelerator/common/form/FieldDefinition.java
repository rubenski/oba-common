package com.obaccelerator.common.form;

import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.obaccelerator.common.ObaConstant.SECRET_VALUE;

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

    public void setValue(List<String> values) {
        this.values = values;
        validate();
    }

    public void applyExistingValue(Map<String, List<String>> existingFormValues) {
        existingFormValues.forEach((existingValueKey, existingValue) -> {
            if (existingValueKey.equals(key)) {
                boolean secret = false;
                if (this instanceof TextInputField) {
                    TextInputField textInputField = (TextInputField) this;
                    if (textInputField.isSecret()) {
                        secret = true;
                    }
                } else if (this instanceof TextAreaInputField) {
                    TextAreaInputField textAreaInputField = (TextAreaInputField) this;
                    if (textAreaInputField.isSecret()) {
                        secret = true;
                    }
                }

                if (secret) {
                    this.values = Collections.singletonList(SECRET_VALUE);
                } else {
                    this.values = existingValue;
                }
            }
        });
    }
}
