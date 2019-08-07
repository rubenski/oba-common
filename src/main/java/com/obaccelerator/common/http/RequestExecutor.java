package com.obaccelerator.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RequestExecutor<I, O> {

    private final RequestBuilder<I> requestBuilder;
    private HttpClient httpClient;
    private Class<O> targetClass;
    private final ResponseValidator[] responseValidators;
    private ObjectMapper objectMapper = new ObjectMapper();

    public RequestExecutor(RequestBuilder<I> requestBuilder, HttpClient httpClient, Class<O> targetClass, ResponseValidator... responseValidators) {
        this.requestBuilder = requestBuilder;
        this.httpClient = httpClient;
        this.targetClass = targetClass;
        this.responseValidators = responseValidators;
    }

    public O execute(I input) {
        HttpUriRequest request = requestBuilder.build(input);
        try {
            return httpClient.execute(request, response -> {
                for (ResponseValidator validator : responseValidators) {
                    validator.validate(response);
                }

                HttpEntity entity = response.getEntity();

                try (InputStream stream = entity.getContent()) {
                    String s = IOUtils.toString(stream, StandardCharsets.UTF_8);
                    // TODO: performance : check GSON, Apache EntityUtils, JSON.simple, Jackson performance. Best performance will be achieved if we don't convert to String first, that is, use Jackon JsonParser, but it is tedious work
                    try {
                        return objectMapper.readValue(s, targetClass);
                    } catch (Exception e) {
                        log.warn("Could not map the following response : " + s);
                        throw new ResponseMappingException("Could not map response from " + request.getURI(), e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RequestExecutionException(e);
        }
    }
}
