package com.obaccelerator.common.error;

import lombok.Getter;

@Getter
public enum ObaError {

    USER_CREATION_FAILED("USR001", "User creation failed", 500),

    TOKEN_INVALID("TKN001", "Token validation failed", 400),
    TOKEN_REQUEST_HAS_NO_KID_HEADER("TKN002", "KID header missing in jws request", 400),
    TOKEN_REQUEST_HAS_NO_CLIENT_ID_CLAIM("TKN003", "Client id claim is missing from jws request", 400),
    TOKEN_NO_KEY_FOUND_FOR_KID("TKN004", "KID sent in kid header doesn't exist at OBA", 400),
    TOKEN_REQUEST_TOKEN_SIGNATURE_INVALID("TKN005", "Request token signature is invalid", 400),
    TOKEN_CLIENT_ID_DOESNT_EXIST("TKN006", "The client id provided in the request token does not exist at OBA", 400),
    TOKEN_REQUEST_TOKEN_PROCESSING_ERROR("TKN007", "Unable to process request token", 400),

    GATEWAY_API_TOKEN_SIGNATURE_INVALID("GTW001", "API token signature is invalid", 400),
    GATEWAY_API_TOKEN_PROCESSING_ERROR("GTW002", "Unable to process API token", 400),
    GATEWAY_API_TOKEN_EXPIRED("GTW003", "API token expired", 400),
    GATEWAY_API_TOKEN_INVALID("GTW004", "API token invalid", 400),

    TECHNICAL_ERROR("OBA001", "A technical error occurred", 500);


    private final String code;
    private String message;
    private int httpStatus;

    ObaError(String code,  String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
