package com.obaccelerator.common.http;

import org.apache.http.HttpResponse;

public interface ResponseValidator {

    void validate(HttpResponse httpResponse) throws ResponseValidationException;
}
