package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class TextInputField extends FieldDefinition {

    private final int minLength;
    private final int maxLength;
    private final boolean required;
    private boolean secret;


    public TextInputField(String key, LabelExplanation labelExplanation, int minLength, int maxLength, boolean required, boolean secret) {
        super(key, labelExplanation, FieldType.TEXT);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
        this.secret = secret;
    }

    public static TextInputField getClientSecretField() {
        return new TextInputField("client_secret", new LabelExplanation("Client Secret"), 3, 300, true, true);
    }

    public static TextInputField getClientIdField() {
        return new TextInputField("client_id", new LabelExplanation("Client ID"), 3, 300, true, false);
    }

    public void markSecret() {
        this.secret = true;
    }

    @Override
    void validate() {
        if ((values == null || values.isEmpty()) && required) {
            fail();
        }
        if (values.get(0).length() < minLength) {
            fail();
        }

        if (values.get(0).length() > maxLength) {
            fail();
        }
    }
}
