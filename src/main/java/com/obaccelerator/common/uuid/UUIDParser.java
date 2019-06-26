package com.obaccelerator.common.uuid;

import java.util.UUID;

public class UUIDParser {

    public static UUID fromString(String uuid) throws UuidInvalidException {
        try {
            return UUID.fromString(uuid);
        } catch (RuntimeException e) {
            throw new UuidInvalidException(e);
        }
    }
}
