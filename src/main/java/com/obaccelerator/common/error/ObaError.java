package com.obaccelerator.common.error;

import lombok.Getter;

@Getter
public enum ObaError {

    USER_CREATION_FAILED("USR", "001", "User creation failed", 500),

    TOKEN_INVALID("TKN", "001", "Token validation failed", 400),
    TOKEN_REQUEST_HAS_NO_KID_HEADER("TKN", "002", "KID header missing in jws request", 400),
    TOKEN_REQUEST_HAS_NO_CLIENT_ID_CLAIM("TKN", "003", "Client id claim is missing from jws request", 400),
    TOKEN_NO_KEY_FOUND_FOR_KID("TKN", "004", "KID sent in kid header doesn't exist at OBA", 400),
    TOKEN_REQUEST_TOKEN_SIGNATURE_INVALID("TKN", "005", "Request token signature is invalid", 400),
    TOKEN_CLIENT_ID_DOESNT_EXIST("TKN", "006", "The client id provided in the request token does not exist at OBA", 400),
    TOKEN_REQUEST_TOKEN_PROCESSING_ERROR("TKN", "007", "Unable to process request token", 400),

    GATEWAY_ACCESS_TOKEN_SIGNATURE_INVALID("GTW", "001", "Access token signature is invalid", 403),
    GATEWAY_ACCESS_TOKEN_PROCESSING_ERROR("GTW", "002", "Unable to process access token", 400),

    TECHNICAL_ERROR("OBA", "001", "A technical error occurred", 500);

    private final String prefix;
    private final String number;
    private String message;
    private int httpStatus;

    ObaError(String prefix, String number, String message, int httpStatus) {
        this.prefix = prefix;
        this.number = number;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
