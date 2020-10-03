package com.obaccelerator.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class RequestExecutor<I, O> {

    private boolean logRequestResponsesOnError;
    private List<ResponseValidator> responseValidators;
    private RequestBuilder<I> requestBuilder;
    private HttpClient httpClient;
    private Class<O> targetClass;
    private static ObjectMapper MAPPER;


    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
    }

    private RequestExecutor() {
    }

    public static class Builder<I, O> {
        private boolean logRequestResponsesOnError;
        private List<ResponseValidator> responseValidators = new ArrayList<>();
        private RequestBuilder<I> requestBuilder;
        private HttpClient httpClient;
        private Class<O> targetClass;

        public Builder(RequestBuilder<I> requestBuilder, HttpClient httpClient, Class<O> targetClass) {
            this.requestBuilder = requestBuilder;
            this.httpClient = httpClient;
            this.targetClass = targetClass;
        }

        public Builder<I, O> logRequestResponsesOnError(boolean logResponsesOnError) {
            this.logRequestResponsesOnError = logResponsesOnError;
            return this;
        }

        public Builder<I, O> addResponseValidator(ResponseValidator responseValidator) {
            this.responseValidators.add(responseValidator);
            return this;
        }

        public RequestExecutor<I, O> build() {
            RequestExecutor<I, O> executor = new RequestExecutor<>();
            executor.httpClient = this.httpClient;
            executor.logRequestResponsesOnError = this.logRequestResponsesOnError;
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
                    try {
                        validator.validate(response);
                    } catch (RuntimeException e) {
                        if (logRequestResponsesOnError) {
                            for (Header header : request.getAllHeaders()) {
                                log.info("Request header: {} : {}", header.getName(), header.getValue());
                            }
                            log.info("Request body: {}", getRequestBodyAsString(request));
                            log.info("Response status {}", response.getStatusLine());
                            String responseBodyAsString = getResponseBodyAsString(response);
                            log.info("Response body: {}", isNotBlank(responseBodyAsString) ? responseBodyAsString : null);
                        }
                        throw e;
                    }
                }

                String responseBodyAsString = getResponseBodyAsString(response);

                // TODO : performance : check GSON, Apache EntityUtils, JSON.simple, Jackson performance. Best
                //  performance will be achieved if we don't convert to String first, that is, use Jackon JsonParser,
                //  but it is tedious work
                try {
                    if (Void.class.equals(targetClass)) {
                        return null;
                    }
                    return MAPPER.readValue(responseBodyAsString, targetClass);
                } catch (Exception e) {
                    throw new ResponseMappingException("Could not map response from " + request.getURI() + (logRequestResponsesOnError ? " body : ~ " + responseBodyAsString + " ~" : ""), e);
                }

            });
        } catch (IOException e) {
            throw new RequestExecutionException(e);
        }
    }

    private String getRequestBodyAsString(HttpUriRequest request) {
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            return getContent(entity);
        }
        return null;
    }

    private String getResponseBodyAsString(HttpResponse httpResponse) {
        if (httpResponse.getEntity() == null) {
            return null;
        }
        return getContent(httpResponse.getEntity());
    }

    private String getContent(HttpEntity entity) {
        try (InputStream content = entity.getContent()) {
            return IOUtils.toString(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Could not get content from entity");
        }
    }

}
