package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
public class CheckBoxesField extends FieldDefinition {

    private final List<CheckBoxValue> checkBoxValues;
    private CheckBoxesMinSelectedValidator checkBoxesMinSelectedValidator;

    public CheckBoxesField(String key, LabelExplanation labelExplanation, List<CheckBoxValue> checkBoxValues) {
        super(key, labelExplanation, FieldType.CHECKBOXES);
        this.checkBoxValues = checkBoxValues;
    }

    public CheckBoxesField(String key, LabelExplanation labelExplanation, List<CheckBoxValue> checkBoxValues,
                           CheckBoxesMinSelectedValidator checkBoxesMinSelectedValidator) {
        super(key, labelExplanation, FieldType.CHECKBOXES);
        this.checkBoxValues = checkBoxValues;
        this.checkBoxesMinSelectedValidator = checkBoxesMinSelectedValidator;
    }


    @Value
    public static class CheckBoxValue {
        String label;
        String value;
        boolean selected;
    }

    @Value
    public static class CheckBoxesMinSelectedValidator {
        int minSelected;
        String message;
    }

    @Override
    void validate() {
        if (checkBoxesMinSelectedValidator != null) {
            if (values == null || values.isEmpty() || values.size() < checkBoxesMinSelectedValidator.minSelected) {
                fail();
            }
        }
    }
}
