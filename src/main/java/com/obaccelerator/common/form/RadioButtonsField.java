package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Value;

import java.util.List;

/**
 * Radio buttons don't work with your current approach for dynamic forms in the front-end. The problem is that in the
 * front-end Angular matched the form controls in the HTML template against an array of controls created in
 * the component. The array of controls also contains values and allows Angular to check/uncheck for example radio
 * buttons. The issue is that in your current approach all form controls are stored in a nested array, with only numeric
 * indices. In order for Angular to match the form controls in the nested array again the form controls in the HTML
 * template, the array indices must perfectly align. Angular uses the [formControlName] value of controls in the HTML
 * template to match againt. With radio buttons this is an issue, because the [formControlName] is also used by
 * the browser as the 'name' of the element. For Radio Buttons the HTML 'name' elements must be identical for
 * all radio buttons, but for Angular they must be different in order to properly match form controls in the backing
 * array with the form component in the HTML template.
 *
 * Long story short : you cannot support radio buttons with your current approach.
 *
 * There is probably a solution to all this : stop working with a single nested array and initialize your form backing
 * object with a (dynamic) field perform component.
 *
 * So instead of creating the backing array like this:
 *     const form = this.fb.group({
 *       all: this.fb.array([])
 *     });
 *
 * It should be created by dynamically adding fields to the form group object:
 *     const form = this.fb.group({
 *       field1: this.fb.array([])
 *       field2: this.fb.array([])
 *       fieldn: this.fb.array([])
 *     });
 *
 *  Check this guy's example of a Radio Button :
 *      https://coryrylan.com/blog/creating-dynamic-radio-lists-with-angular-forms
 *
 *  Check here to learn how to dynamically add fields to an object in Javascript:
 *      https://stackoverflow.com/questions/1184123/is-it-possible-to-add-dynamically-named-properties-to-javascript-object?rq=1
 *
 *
 *   For now, just use a HTML select instead of a Radio Button
 */
@Getter
public class RadioButtonsField extends FieldDefinition {

    private final List<RadioButtonValue> radioButtonValues;
    private final SelectedValidator checkBoxesMinSelectedValidator = new SelectedValidator("You must select an option");

    public RadioButtonsField(String key, LabelExplanation labelExplanation, List<RadioButtonValue> radioButtonValues) {
        super(key, labelExplanation, FieldType.RADIO_BUTTONS_DONT_WORK_SEE_EXPLANATION_IN_OBA_COMMON);
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
