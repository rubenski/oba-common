package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Getter
public class RadioButtonsField extends FieldDefinition {

    private final List<RadioButtonValue> radioButtonValues;
    private final SelectedValidator checkBoxesMinSelectedValidator = new SelectedValidator("You must select an option");

    public RadioButtonsField(String key, LabelExplanation labelExplanation, List<RadioButtonValue> radioButtonValues) {
        super(key, labelExplanation, FieldType.CHECKBOXES);
        this.radioButtonValues = radioButtonValues;
    }

    @Value
    public static class RadioButtonValue {
        String label;
        String value;
    }

    @Value
    public static class SelectedValidator {
        String message;
    }

    @Override
    void validate() {
        if (values == null || values.isEmpty()) {
            fail();
        }
    }
}
