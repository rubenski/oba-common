package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubmittedForm {
    private int stepNr;
    private List<SubmittedValue> values;
}
