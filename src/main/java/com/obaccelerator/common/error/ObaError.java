package com.obaccelerator.common.error;

import lombok.Getter;

import static com.obaccelerator.common.error.ObaError.ErrorMessages.*;

@Getter
public enum ObaError {

    /**
     * OBA errors
     */

    // Application
    APPLICATION_NON_MATCHING_CLIENT_ID_IN_URI_AND_TOKEN("APP001", APP001, APP001, 400),
    APPLICATION_KEYS_MAX_NUMBER_OF_KEYS_REACHED("APP002", APP002, APP002, 403),
    APPLICATION_KEYS_INVALID_KEY("APP004", APP004, APP004, 400),
    APPLICATION_NOT_ONBOARDED_WITH_BANK("APP005", APP005, APP005, 400),
    APPLICATION_ONBOARDING_DISABLED("APP006", APP006, APP006, 403),
    APPLICATION_MAX_NR_OF_APPLICATIONS_REACHED("APP007", APP007, APP007, 403),

    // User
    USER_CREATION_FAILED("USR001", USR001, USR001, 500),

    // Tokens
    TOKEN_INVALID("TKN001", TKN001, TKN001, 400),
    TOKEN_REQUEST_HAS_NO_KID_HEADER("TKN002", TKN002, TKN002, 400),
    TOKEN_REQUEST_HAS_NO_CLIENT_ID_CLAIM("TKN003", TKN003, TKN003, 400),
    TOKEN_NO_KEY_FOUND_FOR_KID("TKN004", TKN004, TKN004, 400),
    TOKEN_AUTHENTICATION_TOKEN_SIGNATURE_INVALID("TKN005", TKN005, TKN005, 400),
    TOKEN_CLIENT_ID_DOESNT_EXIST("TKN006", TKN006, TKN006, 400),
    TOKEN_AUTHENTICATION_TOKEN_PROCESSING_ERROR("TKN007", TKN007, TKN007, 400),
    TOKEN_ELEVATED_AUTHENTICATION_TOKEN_INVALID_ROLE_CLAIM("TKN008", TKN008, TKN008, 400),
    TOKEN_EMPTY("TKN009", TKN009, TKN009, 400),

    // Certificates
    CERTIFICATE_LIMIT_REACHED("CRT001", CRT001, CRT001, 403),

    // Api Registration
    API_REGISTRATION_FORM_INVALID("REG001", REG001, REG001, 400),
    API_REGISTRATION_NOT_FOUND_FOR_ORGANIZATION("REG002", REG002, REG002, 400),

    // Api
    OBA_NO_API_FOR_BANK("API001", "No API for bank", "No API was registered in the Spring context for this bank", 500),

    // Refresh task
    REFRESH_TASK_USER_HAS_NO_REFRESHABLE_CONNECTIONS("RFT001", RFT001, RFT001, 400),

    // Consent session
    CONSENT_EXPECTED_PARAM_MISSING_FROM_REDIRECT_URL("CST001", CST001, CST001, 400),
    CONSENT_SESSION_NOT_FOUND("CST002", CST002, CST002, 400),
    CONSENT_SESSION_ALREADY_CLOSED("CST003", CST003, CST003, 400),
    CONSENT_SESSION_ALREADY_EXISTS_FOR_STATE_PARAM("CST004", CST004, CST004, 400),
    CONSENT_ERROR_WHILE_OBTAINING_ACCESS_TOKEN("CST005", CST005, CST005, 500),
    CONSENT_STATE_PARAM_MISSING("CST006", CST006, CST006, 400),
    CONSENT_STATE_PARAM_INVALID_NO_UUID("CST007", CST007, CST007, 400),

    // Connection
    CONNECTION_CANT_CREATE_CONNECTION_FOR_UNSUCCESSFUL_CONSENT_SESSION("CON001", CON001, CON001, 400),
    CONNECTION_CONSENT_SESSION_NOT_FOUND("CON002", CON002, CON002, 400),
    CONNECTION_ALREADY_EXISTS_FOR_STATE_ID("CON003", CON003, CON003, 400),

    // Redirect URL
    REDIRECT_URL_NOT_FOUND("URL001", URL001, URL001, 400),

    // OBA Generic
    OBA_TECHNICAL_ERROR("OBA001", TECHNICAL_ERROR, TECHNICAL_ERROR, 500),
    OBA_DB_EXCEPTION("OBA002", TECHNICAL_ERROR, "Data access or SQL exception", 500),
    OBA_INVALID_INTERNAL_TOKEN("OBA003", TECHNICAL_ERROR, "Invalid internal token", 500),
    OBA_MISSING_INTERNAL_TOKEN_HEADER("OBA004", TECHNICAL_ERROR, "Missing internal token header", 500),
    OBA_CLIENT_ERROR_INVALID_REQUEST("OBA005", "Invalid client request", "Invalid client request", 400),
    OBA_ENTITY_NOT_FOUND("OBA006", "Entity not found", "Entity not found", 404),
    OBA_CLIENT_ERROR_INVALID_UUID_PROVIDED("OBA007", OBA007, OBA007, 400),
    OBA_INVALID_CONTENT_TYPE("OBA008", OBA008, OBA008, 400),
    OBA_DB_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION("OBA009", TECHNICAL_ERROR, "Db integrity constraint violation", 500),
    OBA_MISSING_AUTHORIZATION_HEADER_ERROR("OBA010", OBA010, OBA010, 400),
    OBA_FILTER_ERROR("OBA011", OBA011, "An exception occurred in a filter", 500),
    OBA_ACCESS_DENIED("OBA012", OBA012, OBA012, 403),
    OBA_REQUEST_METHOD_NOT_SUPPORTED("OBA13", OBA013, OBA013, 405),

    // Security events
    SEM_ILLEGAL_USER_DATA_ACCESS_ATTEMPT("SEM001", "Unauthorized", "Illegal access attempt", 403),

    /**
     * OBA Gateway errors
     */
    GATEWAY_API_TOKEN_SIGNATURE_INVALID("GTW001", GTW001, GTW001, 401),
    GATEWAY_API_TOKEN_MISSING_OR_INVALID_CLAIMS("GTW002", GTW002, GTW002, 401),
    GATEWAY_API_TOKEN_EXPIRED("GTW003", GTW003, GTW003, 401),
    GATEWAY_API_TOKEN_PROCESSING_FAILED("GTW004", GTW004, GTW004, 401),
    GATEWAY_AUTHORIZATION_HEADER_MISSING("GTW005", GTW005, GTW005, 401),
    GATEWAY_NOT_FOUND("GTW006", GTW006, GTW006, 404),
    GATEWAY_TECHNICAL_ERROR("GTW007", GTW007, GTW007, 500),
    GATEWAY_API_TOKEN_INVALID_ROLE_CLAIM("GTW008", GTW008, GTW008, 403),

    /**
     * OBA portal errors
     */
    PORTAL_COULD_NOT_SEND_REGISTRATION_EMAIL("PRT001", TECHNICAL_ERROR, "Could not send registration email", 500),
    PORTAL_INVALID_COGNITO_TOKEN("PRT002", TECHNICAL_ERROR, "Invalid Cognito token received", 500),
    PORTAL_REGISTRATION_ALREADY_EXISTS("PRT003", PRT003, PRT003, 400),
    PORTAL_MISSING_SESSION("PRT004", PRT004, PRT004, 401),
    PORTAL_NOT_AUTHORIZED("PRT005", PRT005, PRT005, 403);

    private final String code;
    private final String clientMessage;
    private final String logMessage;
    private final int httpStatus;

    // Java magic below (avoid illegal forward reference by adding static inner class)
    public static class ErrorMessages {

        // Generic
        public static final String TECHNICAL_ERROR = "Technical error";
        public static final String OBA007 = "One or more fields contain an invalid UUID format";
        public static final String OBA008 = "Invalid content type or missing content-type header";
        public static final String OBA010 = "Authorization header is missing";
        public static final String OBA011 = "Technical error";
        public static final String OBA012 = "Access denied";
        public static final String OBA013 = "Request method not supported";

        // Application
        public static final String APP001 = "Application id in token and uri do not match";
        public static final String APP002 = "You have reached the maximum number of allowed keys";
        public static final String APP004 = "Public key value is not a valid public key";
        public static final String APP005 = "Application is not onboarded with bank";
        public static final String APP006 = "Bank is disabled";
        public static final String APP007 = "The maximum number of applications has been reached";

        // User
        public static final String USR001 = "User creation failed";

        // Tokens
        public static final String TKN001 = "Token validation failed";
        public static final String TKN002 = "KID header missing in jws request";
        public static final String TKN003 = "Client id claim is missing from jws request";
        public static final String TKN004 = "KID sent in token kid header does not exist";
        public static final String TKN005 = "Authentication token signature validation failed";
        public static final String TKN006 = "The client id provided in the authentication token does not exist";
        public static final String TKN007 = "Unable to process authentication token";
        public static final String TKN008 = "Invalid 'role' claim";
        public static final String TKN009 = "Token is empty";

        // Certificates
        public static final String CRT001 = "Certificates limit reached";

        // API registration
        public static final String REG001 = "API registration form validation failed";
        public static final String REG002 = "Api registration not found for organization";

        // Gateway
        public static final String GTW001 = "API token signature invalid";
        public static final String GTW002 = "API token has missing or invalid claims";
        public static final String GTW003 = "API token is expired";
        public static final String GTW004 = "API token processing failed";
        public static final String GTW005 = "Token header missing";
        public static final String GTW006 = "Not found";
        public static final String GTW007 = "Technical error";
        public static final String GTW008 = "API token has an invalid role claim";

        // Refresh tasks
        public static final String RFT001 = "User has no refreshable connections";

        // Consent session
        public static final String CST001 = "Required param missing from consent URL";
        public static final String CST002 = "Consent session not found";
        public static final String CST003 = "Consent session is already closed";
        public static final String CST004 = "Consent session already exists for this state parameter";
        public static final String CST005 = "Error while obtaining access token from bank";
        public static final String CST006 = "State param missing";
        public static final String CST007 = "State param invalid, no UUID";

        // Connection
        public static final String CON001 = "Can't create connection for the provided unsuccessful consent session";
        public static final String CON002 = "Consent session not found for this state id. Cannot create connection";
        public static final String CON003 = "A connection already exists for this consent session's state id";

        // Redirect URL
        public static final String URL001 = "Provided redirect URL not found";

        // Portal
        public static final String PRT003 = "Registration already exists";
        public static final String PRT004 = "Session t okmissing";
        public static final String PRT005 = "Not authorized";

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

    public static boolean isCustomObaError(String code) {
        for (ObaError value : values()) {
            if (value.getCode().equals(code)) {
                return true;
            }

        }
        return false;
    }
}
