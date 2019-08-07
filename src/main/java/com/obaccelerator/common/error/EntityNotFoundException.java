package com.obaccelerator.common.error;

import java.lang.reflect.Type;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Type clazz) {
        super(clazz.getTypeName() + " not found");
    }
}
