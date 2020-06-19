package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class TextInputField extends FieldDefinition {

    private final String regex;
    private final int minLength;
    private final int maxLength;
    private final boolean required;

    public TextInputField(String key, LabelExplanation labelExplanation, String regex, int minLength, int maxLength, boolean required) {
        super(key, labelExplanation, FieldType.TEXT);
        this.regex = regex;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
    }

    public static TextInputField getClientSecretField() {
        return new TextInputField("client_secret", new LabelExplanation("Client Secret"), null, 3,
                300, true);
    }

    public static TextInputField getClientIdField() {
        return new TextInputField("client_id", new LabelExplanation("Client ID"), null, 3,
                300, true);
    }
}
