package com.obaccelerator.common.form;

import lombok.Value;

import java.util.List;

@Value
public class SubmittedForm {
    private List<FormValue> formValues;
}
