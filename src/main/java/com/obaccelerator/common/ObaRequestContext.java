package com.obaccelerator.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@AllArgsConstructor
@Getter
public class ObaRequestContext {
    private String clientId;

    public UUID getClientId() {
        return UUID.fromString(clientId);
    }
}
