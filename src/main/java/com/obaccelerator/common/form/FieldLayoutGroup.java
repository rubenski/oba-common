package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class FieldLayoutGroup {

    private List<FieldDefinition> fields = new ArrayList<>();

    public FieldLayoutGroup(List<FieldDefinition> fields) {
        this.fields = fields;
    }


}
