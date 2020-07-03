package com.obaccelerator.common.model.organization;

import lombok.Value;

import java.util.UUID;

@Value
public class OrganizationId {
    UUID id;

    public String asString() {
        return id.toString();
    }


}
