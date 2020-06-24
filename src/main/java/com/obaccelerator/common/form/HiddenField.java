package com.obaccelerator.common.form;

import lombok.Getter;

import java.util.List;

@Getter
public class HiddenField extends FieldDefinition {

    public HiddenField(String key, LabelExplanation labelExplanation, List<String> values) {
        super(key, labelExplanation, FieldType.HIDDEN);
        this.values = values;
    }

    @Override
    void validate() {

    }
}
