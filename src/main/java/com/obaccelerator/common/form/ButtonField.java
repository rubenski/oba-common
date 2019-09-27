package com.obaccelerator.common.form;



public class ButtonField extends FieldDefinition {

    private String buttonText;

    public ButtonField(String buttonText) {
        this.buttonText = buttonText;
    }

    @Override
    public FieldType getType() {
        return FieldType.BUTTON;
    }
}
