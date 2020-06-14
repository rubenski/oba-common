package com.obaccelerator.common.form;

import lombok.Value;

import java.util.Map;

@Value
public class CheckBoxesField extends FieldDefinition {

    LabelExplanation labelExplanation;
    Map<String, String> values;

    @Override
    FieldType getType() {
        return FieldType.CHECKBOXES;
    }

    @Override
    LabelExplanation getLabelExplanation() {
        return labelExplanation;
    }
}
