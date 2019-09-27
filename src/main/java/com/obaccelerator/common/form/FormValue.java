package com.obaccelerator.common.form;

import lombok.Value;

import java.util.List;

@Value
public class FormValue {
    private String name;
    private List<String> values;
}
