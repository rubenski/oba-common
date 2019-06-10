package com.obaccelerator.common.token;

import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;

public class ApiTokenValidator {

    private static final String TOKEN_INVALID_MESSAGE_CLAIMS = "API token invalid: wrong number of claims. API tokens " +
            "are generated by OBA and should always be valid.";
    private static final String TOKEN_INVALID_MESSAGE_UUID = "API token invalid: cannot parse client UUID. API tokens " +
            "are generated by OBA and should always be valid.";
    private static final String TOKEN_INVALID_SIGNATURE = "API token invalid: signature is invalid. API tokens are " +
            "generated by OBA and should always be valid.";
    private static final String TOKEN_EXPIRED_MESSAGE = "API token expired";
    private TokenReader apiTokenReader;

    public ApiTokenValidator(TokenReader apiTokenReader) {
        this.apiTokenReader = apiTokenReader;
    }

    public void validateToken(final String token, final PublicKey publicKey) throws ApiTokenException {

        boolean b = apiTokenReader.verifySignature(token, publicKey);

        if (!b) {
            throw new ApiTokenValidationException(TOKEN_INVALID_SIGNATURE);
        }

        Map<String, Object> claims = apiTokenReader.readClaims(token, publicKey);

        // Are all expected fields present?
        Object iat = claims.get("iat");
        Object clientId = claims.get("client_id");
        Object jti = claims.get("jti");
        Object exp = claims.get("exp");

        if (claims.size() != 4 || iat == null || clientId == null || jti == null || exp == null) {
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
            throw new ApiTokenExpiredException(TOKEN_EXPIRED_MESSAGE);
        }
    }
}