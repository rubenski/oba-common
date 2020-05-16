package com.obaccelerator.common.rest;

import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class RestResponse {
    UUID requestId;

    protected abstract UUID getId();

    protected abstract OffsetDateTime getCreated();
}
