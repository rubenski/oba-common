package com.obaccelerator.common.model.application;

import lombok.Value;

import java.util.UUID;

@Value
public class ApplicationId {
    UUID id;

    public String asString() {
        return id.toString();
    }
}
