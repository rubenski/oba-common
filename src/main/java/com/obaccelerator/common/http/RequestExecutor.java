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
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RequestExecutor<I, O> {

    private boolean logResponses;
    private List<ResponseValidator> responseValidators;
    private RequestBuilder<I> requestBuilder;
    private HttpClient httpClient;
    private Class<O> targetClass;
    private static ObjectMapper MAPPER;


    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
    }


    private RequestExecutor() { }

    public static class Builder<I,O> {
        private boolean logResponses;
        private List<ResponseValidator> responseValidators = new ArrayList<>();
        private RequestBuilder<I> requestBuilder;
        private HttpClient httpClient;
        private Class<O> targetClass;

        public Builder(RequestBuilder<I> requestBuilder, HttpClient httpClient, Class<O> targetClass) {
            this.requestBuilder = requestBuilder;
            this.httpClient = httpClient;
            this.targetClass = targetClass;
        }

        public Builder<I,O> logResponses(boolean logResponses) {
            this.logResponses = logResponses;
            return this;
        }

        public Builder<I,O> addResponseValidator(ResponseValidator responseValidator) {
            this.responseValidators.add(responseValidator);
            return this;
        }

        public RequestExecutor<I,O> build() {
            RequestExecutor<I,O> executor = new RequestExecutor<>();
            executor.httpClient = this.httpClient;
            executor.logResponses = this.logResponses;
            executor.responseValidators = this.responseValidators;
            executor.targetClass = this.targetClass;
            executor.requestBuilder = this.requestBuilder;
            return executor;
        }
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
                        return MAPPER.readValue(s, targetClass);
                    } catch (Exception e) {
                        throw new ResponseMappingException("Could not map response from " + request.getURI() + (logResponses ? " : " + s : ""), e);
                    }
                }
            });
        } catch (IOException e) {
            throw new RequestExecutionException(e);
        }
    }

}
