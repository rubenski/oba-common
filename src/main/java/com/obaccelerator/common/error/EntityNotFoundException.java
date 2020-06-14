package com.obaccelerator.common.error;

import java.lang.reflect.Type;
import java.util.UUID;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Type clazz, UUID id) {
        super(clazz.getTypeName() + " with id " + id.toString() + " not found");
    }

    public EntityNotFoundException(Type clazz, String systemName) {
        super(clazz.getTypeName() + " with id " + systemName +  " not found");
    }

    public EntityNotFoundException(Type clazz) {
        super(clazz.getTypeName() + " not found");
    }
}
