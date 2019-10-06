package com.obaccelerator.common.error;

import java.util.UUID;

public class EntityNotFoundAfterInsertException extends RuntimeException {

    public EntityNotFoundAfterInsertException(UUID id) {
        super("Could not find entity with id " + id + " right after it was or should have been inserted");
    }
}
