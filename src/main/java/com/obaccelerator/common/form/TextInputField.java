package com.obaccelerator.common.form;

import lombok.Getter;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
public class TextInputField extends FieldDefinition {

    private final int minLength;
    private final int maxLength;
    private final boolean required;

    public TextInputField(String key, LabelExplanation labelExplanation, int minLength, int maxLength, boolean required) {
        super(key, labelExplanation, FieldType.TEXT);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.required = required;
    }

    public static TextInputField getClientSecretField() {
        return new TextInputField("client_secret", new LabelExplanation("Client Secret"), 3, 300, true);
    }

    public static TextInputField getClientIdField() {
        return new TextInputField("client_id", new LabelExplanation("Client ID"), 3,
                300, true);
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
