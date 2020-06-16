package com.obaccelerator.common.form;


import lombok.Getter;

@Getter
public class ButtonField extends FieldDefinition {

    private final String buttonText;

    public ButtonField(String key, LabelExplanation labelExplanation, String buttonText) {
        super(key, labelExplanation);
        this.buttonText = buttonText;
    }

    FieldType getType() {
        return FieldType.BUTTON;
    }

}
