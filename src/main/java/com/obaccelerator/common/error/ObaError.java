package com.obaccelerator.common.error;

import lombok.Getter;

import static com.obaccelerator.common.error.ObaError.ErrorMessages.*;

@Getter
public enum ObaError {

    /**
     * OBA errors
     */

    // Client
    CLIENT_NON_MATCHING_CLIENT_ID_IN_URI_AND_TOKEN("CLT001", CLT001, CLT001, 400),
    CLIENT_KEYS_MAX_NUMBER_OF_KEYS_REACHED("CLT002", CLT002, CLT002, 409),
    CLIENT_KEYS_KEY_ID_ALREADY_EXISTS("CLT003", CLT003, CLT003, 409),
    CLIENT_KEYS_INVALID_KEY("CLT004", CLT004, CLT004, 400),

    // User
    USER_CREATION_FAILED("USR001", USR001, USR001, 500),

    // Tokens
    TOKEN_INVALID("TKN001", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN001, 400),
    TOKEN_REQUEST_HAS_NO_KID_HEADER("TKN002", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN002, 400),
    TOKEN_REQUEST_HAS_NO_CLIENT_ID_CLAIM("TKN003", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN003, 400),
    TOKEN_NO_KEY_FOUND_FOR_KID("TKN004", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN004, 400),
    TOKEN_AUTHENTICATION_TOKEN_SIGNATURE_INVALID("TKN005", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN005, 400),
    TOKEN_CLIENT_ID_DOESNT_EXIST("TKN006", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN006, 400),
    TOKEN_AUTHENTICATION_TOKEN_PROCESSING_ERROR("TKN007", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN007, 400),
    TOKEN_ELEVATED_AUTHENTICATION_TOKEN_INVALID_ROLE_CLAIM("TKN008", TKN_CLIENT_MESSAGE_INVALID_TOKEN, TKN008, 400),

    // OBA Generic
    OBA_TECHNICAL_ERROR("OBA001", TECHNICAL_ERROR, TECHNICAL_ERROR, 500),
    OBA_DB_EXCEPTION("OBA002", TECHNICAL_ERROR, "Data access or SQL exception", 500),
    OBA_INVALID_INTERNAL_TOKEN("OBA003", TECHNICAL_ERROR, "Invalid internal token", 500),
    OBA_MISSING_INTERNAL_TOKEN_HEADER("OBA004", TECHNICAL_ERROR, "Missing internal token header", 500),
    OBA_CLIENT_ERROR_INVALID_REQUEST("OBA005", "Invalid client request", "Invalid client request", 400),
    OBA_ENTITY_NOT_FOUND("OBA006", "Entity not found", "Entity not found", 404),
    OBA_CLIENT_ERROR_INVALID_UUID_PROVIDED("OBA007", OBA007, OBA007, 400),


    /**
     * OBA Gateway errors
     */
    GATEWAY_API_TOKEN_SIGNATURE_INVALID("GTW001", GTW001, GTW001, 400),
    GATEWAY_API_TOKEN_PROCESSING_ERROR("GTW002", GTW002, GTW002, 400),
    GATEWAY_API_TOKEN_EXPIRED("GTW003", GTW003, GTW003, 400),
    GATEWAY_API_TOKEN_INVALID("GTW004", GTW004, GTW004, 400),


    /**
     * OBA portal errrors
     */
    PORTAL_COULD_NOT_SEND_REGISTRATION_EMAIL("PRT001", TECHNICAL_ERROR, "Could not send registration email", 500),
    PORTAL_INVALID_COGNITO_TOKEN("PRT002", TECHNICAL_ERROR, "Invalid Cognito token received", 500);


    private final String code;
    private String clientMessage;
    private String logMessage;
    private int httpStatus;

    // Java magic below (avoid illegal forward reference by adding static inner class)
    public static class ErrorMessages {

        // Generic
        public static final String TECHNICAL_ERROR = "Technical error";
        public static final String OBA007 = "One or more fields contain an invalid UUID format";

        // Client
        public static final String CLT001 = "Client id in token and uri do not match";
        public static final String CLT002 = "You have reached the maximum number of allowed keys";
        public static final String CLT003 = "Key id already exists";
        public static final String CLT004 = "Public key value is not a valid public key";

        // User
        public static final String USR001 = "User creation failed";

        // Tokens
        public static final String TKN_CLIENT_MESSAGE_INVALID_TOKEN = "Invalid token";
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
