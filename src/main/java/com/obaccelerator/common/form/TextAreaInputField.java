package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class TextAreaInputField extends FieldDefinition {

    private String regex;
    private Integer minLength;
    private Integer maxLength;
    private int rows;
    private boolean required;
    private boolean secret;

    private TextAreaInputField(String key, LabelExplanation labelExplanation) {
        super(key, labelExplanation, FieldType.TEXT_AREA);
    }

    @Override
    void validate() {
        if (!values.isEmpty() && values.get(0).length() < minLength) {
            throw new ApiRegistrationFormValidationException(key);
        }
    }

    public static class Builder {
        private final String key;
        private final LabelExplanation labelExplanation;
        private String regex;
        private Integer minLength;
        private Integer maxLength;
        private int rows = 5;
        private boolean required;
        private boolean secret;

        public Builder(String key, LabelExplanation labelExplanation) {
            this.key = key;
            this.labelExplanation = labelExplanation;
        }

        public Builder setRegex(String regex) {
            this.regex = regex;
            return this;
        }

        public Builder setMinLength(Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        public Builder setRows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder setSecret(boolean secret) {
            this.secret = secret;
            return this;
        }

        public TextAreaInputField build() {
            TextAreaInputField field = new TextAreaInputField(key, labelExplanation);
            field.maxLength = maxLength;
            field.minLength = minLength;
            field.rows = rows;
            field.regex = regex;
            field.required = required;
            field.secret = secret;
            return field;
        }
    }
}
