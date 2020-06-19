package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class HiddenField extends FieldDefinition {

    public HiddenField(String key, LabelExplanation labelExplanation, String value) {
        super(key, labelExplanation, FieldType.HIDDEN);
        this.value = value;
    }
}
