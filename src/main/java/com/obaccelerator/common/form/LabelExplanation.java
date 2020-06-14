package com.obaccelerator.common.form;

import lombok.Getter;

@Getter
public class LabelExplanation {
    String label;
    String explanation;

    public LabelExplanation(String label) {
        this.label = label;
        this.explanation = null;
    }

    public LabelExplanation(String label, String explanation) {
        this.label = label;
        this.explanation = explanation;
    }
}
