package com.obaccelerator.common.form;

public abstract class FieldDefinition {

    abstract FieldType getType();
    abstract LabelExplanation getLabelExplanation();

    public static TextInputField getClientSecretField() {
        return new TextInputField(new LabelExplanation("Client secret"), null,
                5, 300, true);
    }

    public static TextInputField getClientIdField() {
        return new TextInputField(new LabelExplanation("Client id"), null,
                5, 300, true);
    }

}
