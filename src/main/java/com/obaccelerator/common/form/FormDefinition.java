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
        if(title == null) {
            throw new IllegalArgumentException("Empty title");
        }

        if(fieldLayoutGroups == null) {
            throw new IllegalArgumentException("Empty layout groups");
        }
        this.title = title;
        this.explanation = explanation;
        this.fieldLayoutGroups = fieldLayoutGroups;
    }

    public FormDefinition(String title, List<FieldLayoutGroup> fieldLayoutGroups) {

        if(title == null) {
            throw new IllegalArgumentException("Empty title");
        }

        if(fieldLayoutGroups == null) {
            throw new IllegalArgumentException("Empty layout groups");
        }
        this.title = title;
        this.fieldLayoutGroups = fieldLayoutGroups;
    }

    public FormDefinition(List<FieldLayoutGroup> fieldLayoutGroups) {
        this.fieldLayoutGroups = fieldLayoutGroups;
    }

    /**
     * TODO: This is no longer used, but it ultimately calls validation... We MUST call validation on the form fields!!!
     * @param submittedForm
     */
    public void applySubmittedForm(SubmittedForm submittedForm) {
        fieldLayoutGroups.stream()
                .flatMap(flg -> flg.getFields().stream())
                .filter(f -> submittedForm.getValues().stream()
                        .filter(sv -> f.key.equals(sv.getKey())).findAny()
                        .map(sv -> {
                            f.setValue(sv.getValues());
                            return true;
                        })
                        .orElseThrow(() -> new FieldNotSubmittedException(f.key)));
    }
}
