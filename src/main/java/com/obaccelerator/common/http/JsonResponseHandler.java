package com.obaccelerator.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;

public class JsonResponseHandler<T> implements ResponseHandler<T> {

    private Class<T> targetClass;
    private ResponseValidator[] validators;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JsonResponseHandler(Class<T> targetClass, ResponseValidator... validators) {
        this.targetClass = targetClass;
        this.validators = validators;
    }

    @Override
    public T handleResponse(HttpResponse response) throws IOException {
        for (ResponseValidator validator : validators) {
            validator.validate(response);
        }

        HttpEntity entity = response.getEntity();

        try (InputStream instream = entity.getContent()) {
            // TODO: performance : check GSON, Apache EntityUtils, JSON.simple, Jackson performance. Best performance will be achieved if we don't convert to String first, that is, use Jackon JsonParser, but it is tedious work
            return objectMapper.convertValue(instream, targetClass);
        }
    }
}
