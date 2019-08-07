package com.obaccelerator.common.http;

import org.apache.http.HttpResponse;

public class ResponseNotEmptyValidator implements ResponseValidator {
    @Override
    public void validate(HttpResponse httpResponse) {
        if(httpResponse.getEntity() == null) {
            throw new ResponseValidationException("Response was empty");
        }
    }
}
