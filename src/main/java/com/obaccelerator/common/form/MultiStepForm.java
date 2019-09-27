package com.obaccelerator.common.form;

import lombok.Value;

import java.util.List;

@Value
public class MultiStepForm {
    private List<FormDefinition> formDefinitions;
}
