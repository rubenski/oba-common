package com.obaccelerator.common.http;

import org.apache.http.HttpResponse;

public class ExpectedHttpCodesValidator implements ResponseValidator {

    private int[] validCodes;

    public ExpectedHttpCodesValidator(int... validCodes) {
        this.validCodes = validCodes;
    }


    @Override
    public void validate(HttpResponse httpResponse) {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        for (int validCode : validCodes) {
            if(validCode == statusCode) {
                return;
            }
        }
        throw new ResponseValidationException("Received invalid HTTP status " + statusCode);
    }
}
