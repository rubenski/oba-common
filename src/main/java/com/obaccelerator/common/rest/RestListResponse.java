package com.obaccelerator.common.rest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public abstract class RestListResponse<T> {
    UUID requestId;
    OffsetDateTime time;
    List<T> result;
}
