package com.obaccelerator.common.form;

import lombok.Value;

@Value
public class DescriptionField extends FieldDefinition {

    private String description;

    @Override
    FieldType getType() {
        return FieldType.DESCRIPTION;
    }
}
