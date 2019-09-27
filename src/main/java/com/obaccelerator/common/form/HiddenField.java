package com.obaccelerator.common.form;

import lombok.Value;

@Value
public class HiddenField extends FieldDefinition {

    private String value;

    @Override
    public FieldType getType() {
        return FieldType.HIDDEN;
    }
}
