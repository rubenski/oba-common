package com.obaccelerator.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpUriRequest;

public interface RequestBuilder<I> {
    ObjectMapper MAPPER = new ObjectMapper();

    HttpUriRequest build(I input);
}
