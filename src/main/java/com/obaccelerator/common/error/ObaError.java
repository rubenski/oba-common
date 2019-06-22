package com.obaccelerator.common.error;

import lombok.Getter;

import static com.obaccelerator.common.error.ObaError.ErrorMessages.*;

@Getter
public enum ObaError {


    USER_CREATION_FAILED("USR001", USR001, USR001, 500),

    TOKEN_INVALID("TKN001", TKN001, TKN001, 400),
    TOKEN_REQUEST_HAS_NO_KID_HEADER("TKN002", TKN002, TKN002, 400),
    TOKEN_REQUEST_HAS_NO_CLIENT_ID_CLAIM("TKN003", TKN003, TKN003, 400),
    TOKEN_NO_KEY_FOUND_FOR_KID("TKN004", TKN004, TKN004, 400),
    TOKEN_AUTHENTICATION_TOKEN_SIGNATURE_INVALID("TKN005", TKN005, TKN005, 400),
    TOKEN_CLIENT_ID_DOESNT_EXIST("TKN006", TKN006, TKN006, 400),
    TOKEN_AUTHENTICATION_TOKEN_PROCESSING_ERROR("TKN007", TKN007, TKN007, 400),
    TOKEN_ELEVATED_AUTHENTICATION_TOKEN_INVALID_ROLE_CLAIM("TKN008", TKN008, TKN008, 400),

    GATEWAY_API_TOKEN_SIGNATURE_INVALID("GTW001", GTW001, GTW001, 400),
    GATEWAY_API_TOKEN_PROCESSING_ERROR("GTW002", GTW002, GTW002, 400),
    GATEWAY_API_TOKEN_EXPIRED("GTW003", GTW003, GTW003, 400),
    GATEWAY_API_TOKEN_INVALID("GTW004", GTW004, GTW004, 400),

    OBA_TECHNICAL_ERROR("OBA001", GENERIC_TECHNICAL_ERROR, GENERIC_TECHNICAL_ERROR, 500),
    OBA_DB_EXCEPTION("OBA002", GENERIC_TECHNICAL_ERROR, "Data access or SQL exception", 500),
    OBA_INVALID_INTERNAL_TOKEN("OBA003", GENERIC_TECHNICAL_ERROR, "Invalid internal token", 500),
    OBA_MISSING_INTERNAL_TOKEN_HEADER("OBA004", GENERIC_TECHNICAL_ERROR, "Missing internal token header", 500);

    private final String code;
    private String clientMessage;
    private String logMessage;
    private int httpStatus;

    // Java magic below (avoid illegal forward reference by adding static inner class)
    public static class ErrorMessages {

        // Generic
        public static final String GENERIC_TECHNICAL_ERROR = "Technical error";

        // User
        public static final String USR001 = "User creation failed";

        // Token
        public static final String TKN001 = "Token validation failed";
        public static final String TKN002 = "KID header missing in jws request";
        public static final String TKN003 = "Client id claim is missing from jws request";
        public static final String TKN004 = "KID sent in token kid header does not exist";
        public static final String TKN005 = "Authentication token signature is invalid";
        public static final String TKN006 = "The client id provided in the authentication token does not exist";
        public static final String TKN007 = "Unable to process authentication token";
        public static final String TKN008 = "Invalid 'role' claim";

        // Gateway
        public static final String GTW001 = "API token signature is invalid";
        public static final String GTW002 = "Unable to process API token";
        public static final String GTW003 = "API token expired";
        public static final String GTW004 = "API token expired";
    }

    ObaError(String code, String clientMessage, String logMessage, int httpStatus) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.logMessage = logMessage;
        this.httpStatus = httpStatus;
    }

    public String getFormattedLogMessage() {
        return String.format("[ %d | %s | %s | %s ]", getHttpStatus(), getCode(), getClientMessage(), getLogMessage());
    }


}
