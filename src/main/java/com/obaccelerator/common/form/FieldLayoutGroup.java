package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class FieldLayoutGroup {

    public FieldLayoutGroup(String groupTitle, LayoutDirection layoutDirection, List<FieldDefinition> fields) {
        this.groupTitle = groupTitle;
        this.layoutDirection = layoutDirection;
        this.fields = fields;
    }

    public FieldLayoutGroup(LayoutDirection layoutDirection, List<FieldDefinition> fields) {
        this.layoutDirection = layoutDirection;
        this.fields = fields;
    }

    private String groupTitle;
    private LayoutDirection layoutDirection;
    private List<FieldDefinition> fields = new ArrayList<>();
}
