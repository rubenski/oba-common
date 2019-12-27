package com.obaccelerator.common.token;

import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Validates API tokens that OBA created and provided to the client
 */
public class TokenValidator {

    private TokenValidator() {
    }

    private static final String TOKEN_INVALID_MESSAGE_CLAIMS = "API token invalid: wrong number of claims";
    private static final String TOKEN_INVALID_MESSAGE_UUID = "API token invalid: cannot parse client UUID. API tokens";
    private static final String TOKEN_INVALID_SIGNATURE = "API token has an invalid signature";
    private static final String TOKEN_EXPIRED_MESSAGE = "API token expired";
    private static final String TOKEN_PROCESSING_EXCEPTION_MESSAGE = "Could not process token";

    public static void validateToken(final String token, final PublicKey publicKey) throws ApiTokenValidationException {

        verifySignature(token, publicKey);

        Map<String, Object> claims = null;
        try {
            claims = TokenReader.readClaims(token, publicKey);
        } catch (ApiTokenProcessingException e) {
            throw new ApiTokenValidationException(TOKEN_PROCESSING_EXCEPTION_MESSAGE);
        }

        // Are all expected fields present?
        Object iat = claims.get("iat");
        Object clientId = claims.get("client_id");
        Object jti = claims.get("jti");
        Object exp = claims.get("exp");
        Object role = claims.get("role");

        if (claims.size() != 5 || iat == null || isBlank((String) clientId) || isBlank((String) jti)||
                exp == null || isBlank((String) role)) {
            throw new ApiTokenValidationException(TOKEN_INVALID_MESSAGE_CLAIMS);
        }

        // Is the client_id a real UUID?
        try {
            UUID.fromString((String) clientId);
        } catch (RuntimeException e) {
            throw new ApiTokenValidationException(TOKEN_INVALID_MESSAGE_UUID);
        }

        // Is the token expired?
        long expTime = (long) exp;
        if (expTime < System.currentTimeMillis()) {
            throw new ApiTokenValidationException(TOKEN_EXPIRED_MESSAGE);
        }
    }

    private static void verifySignature(String token, PublicKey publicKey) throws ApiTokenValidationException {
        try {
            TokenReader.verifySignature(token, publicKey);
        } catch (ApiTokenSignatureValidationException e) {
            throw new ApiTokenValidationException(TOKEN_INVALID_SIGNATURE);
        } catch (ApiTokenProcessingException e) {
            throw new ApiTokenValidationException(TOKEN_PROCESSING_EXCEPTION_MESSAGE);
        }
    }
}
