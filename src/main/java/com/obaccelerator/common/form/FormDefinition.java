package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class FormDefinition {
    private String title;
    private String explanation;
    private List<FieldLayoutGroup> fieldLayoutGroups;

    public FormDefinition(String title, String explanation, List<FieldLayoutGroup> fieldLayoutGroups) {
        this.title = title;
        this.explanation = explanation;
        this.fieldLayoutGroups = fieldLayoutGroups;
    }

    public FormDefinition(String title, List<FieldLayoutGroup> fieldLayoutGroups) {
        this.title = title;
        this.fieldLayoutGroups = fieldLayoutGroups;
    }

    public FormDefinition(List<FieldLayoutGroup> fieldLayoutGroups) {
        this.fieldLayoutGroups = fieldLayoutGroups;
    }
}
