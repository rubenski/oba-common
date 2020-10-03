package com.obaccelerator.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
public class SimpleRequestExecutor {

    private boolean logRequestResponsesOnError;
    private boolean logSuccessfulRequestsAndResponses;
    private List<ResponseValidator> responseValidators;
    private HttpClient httpClient;
    private static ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.findAndRegisterModules();
    }

    private SimpleRequestExecutor() {
    }

    public static class Builder {
        private boolean logRequestResponsesOnError;
        private boolean logSuccessfulRequestsAndResponses;
        private List<ResponseValidator> responseValidators = new ArrayList<>();
        private HttpClient httpClient;

        public Builder(HttpClient httpClient) {
            this.httpClient = httpClient;
        }

        public SimpleRequestExecutor.Builder logRequestResponsesOnError(boolean logResponsesOnError) {
            this.logRequestResponsesOnError = logResponsesOnError;
            return this;
        }

        public SimpleRequestExecutor.Builder logSuccessfulRequestsAndResponses(boolean logSuccessfulRequestsAndResponses) {
            this.logSuccessfulRequestsAndResponses = logSuccessfulRequestsAndResponses;
            return this;
        }

        public SimpleRequestExecutor.Builder addResponseValidator(ResponseValidator responseValidator) {
            this.responseValidators.add(responseValidator);
            return this;
        }

        public SimpleRequestExecutor build() {
            SimpleRequestExecutor executor = new SimpleRequestExecutor();
            executor.httpClient = this.httpClient;
            executor.logRequestResponsesOnError = this.logRequestResponsesOnError;
            executor.logSuccessfulRequestsAndResponses = this.logSuccessfulRequestsAndResponses;
            executor.responseValidators = this.responseValidators;
            return executor;
        }
    }

    public <O> O execute(HttpUriRequest request, Class<O> clazz) throws RequestExecutionException {
        try {
            return httpClient.execute(request, response -> {
                String responseBodyAsString = getResponseBodyAsString(response);
                for (ResponseValidator validator : responseValidators) {
                    try {
                        validator.validate(response);
                    } catch (ResponseValidationException e) {
                        logRequestResponse(request, response, responseBodyAsString);
                        throw e;
                    }
                }

                // TODO : performance : check GSON, Apache EntityUtils, JSON.simple, Jackson performance. Best
                //  performance will be achieved if we don't convert to String first, that is, use Jackon JsonParser,
                //  but it is tedious work
                try {
                    if (Void.class.equals(clazz)) {
                        return null;
                    }
                    return MAPPER.readValue(responseBodyAsString, clazz);
                } catch (Exception e) {
                    if(logRequestResponsesOnError)  {
                        logRequestResponse(request, response, responseBodyAsString);
                    }
                    throw new ResponseMappingException("Could not map response from " + request.getURI(), e);
                } finally {
                    if(logSuccessfulRequestsAndResponses)  {
                        logRequestResponse(request, response, responseBodyAsString);
                    }
                }

            });
        } catch (IOException e) {
            throw new RequestExecutionException(e);
        }
    }

    private void logRequestResponse(HttpUriRequest request, HttpResponse response, String responseBodyAsString) {
        log.info("\n\nREQUEST");
        log.info("Request to : {} {}", request.getMethod(), request.getURI());
        for (Header header : request.getAllHeaders()) {
            log.info("(header) {} : {}", header.getName(), header.getValue());
        }
        log.info("(body) " + getRequestBodyAsString(request));

        log.info("\nRESPONSE");
        log.info("(status) {}", response.getStatusLine());
        for (Header header : response.getAllHeaders()) {
            log.info("(header) {} : {}", header.getName(), header.getValue());
        }
        log.info("(body): \n{}", isNotBlank(responseBodyAsString) ? responseBodyAsString : "<empty>" );
        log.info("\n");
    }

    private String getRequestBodyAsString(HttpUriRequest request) {
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            if (entity == null) {
                return null;
            }
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
            throw new RuntimeException("Could not get content from entity" );
        }
    }
}
