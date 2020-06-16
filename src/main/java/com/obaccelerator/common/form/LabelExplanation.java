package com.obaccelerator.common.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
