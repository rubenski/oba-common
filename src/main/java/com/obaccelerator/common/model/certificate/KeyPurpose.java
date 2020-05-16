package com.obaccelerator.common.model.certificate;

public enum KeyPurpose {
    SIGNING("signing"), TRANSPORT("transport");

    private final String purpose;

    KeyPurpose(String purpose) {
        this.purpose = purpose;
    }
}
