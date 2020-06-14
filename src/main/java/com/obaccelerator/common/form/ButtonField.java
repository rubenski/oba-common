package com.obaccelerator.common.form;


import lombok.Value;

@Value
public class ButtonField extends FieldDefinition {

    LabelExplanation labelExplanation;
    String buttonText;

    @Override
    public FieldType getType() {
        return FieldType.BUTTON;
    }
}
