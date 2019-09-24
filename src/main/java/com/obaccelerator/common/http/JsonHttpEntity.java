package com.obaccelerator.common.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.*;

/**
 * Simple wrapper around Apache HTTP StringEntity that deserializes a Java POJO into a String
 * @param <T>
 */
public class JsonHttpEntity<T> extends AbstractHttpEntity {

    private StringEntity stringEntity;

    public JsonHttpEntity(T entity) {
        String request;
        try {
            request = new ObjectMapper().writer().writeValueAsString(entity);
            stringEntity = new StringEntity(request, ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isRepeatable() {
        return stringEntity.isRepeatable();
    }

    @Override
    public long getContentLength() {
        return stringEntity.getContentLength();
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return stringEntity.getContent();
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        stringEntity.writeTo(outStream);
    }

    @Override
    public boolean isStreaming() {
        return stringEntity.isStreaming();
    }

    @Override
    public Header getContentType() {
        return stringEntity.getContentType();
    }
}
