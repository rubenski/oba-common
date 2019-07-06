package com.obaccelerator.common.client;

import com.obaccelerator.common.ObaRequestContext;

import java.util.UUID;

public class ClientIdConsistencyChecker {

    public static void isConsistent(ObaRequestContext requestContext, UUID clientUuid) {
        if(!requestContext.getClientId().equals(clientUuid)) {
            throw new InconsistentClientIdException();
        }
    }
}
