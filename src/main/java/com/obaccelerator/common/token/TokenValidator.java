package com.obaccelerator.common.token;

import com.obaccelerator.common.uuid.UUIDParser;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.APPLICATION_ID_CLAIM;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Validates API tokens that OBA created and provided to the client
 */
public class TokenValidator {

    private TokenValidator() {
    }

    public static void validateToken(final HttpServletRequest request, final PublicKey publicKey) throws ApiTokenProcessingException,
            ApiTokenMissingOrInvalidClaimsException, ApiTokenExpiredException, ApiTokenInvalidSignatureException, ApiTokenMissingException {


        Optional<String> tokenOptional = TokenReader.getTokenFromHeader(request);
        if (tokenOptional.isEmpty()) {
            throw new ApiTokenMissingException();
        }

        validateToken(tokenOptional.get(), publicKey);
    }


    public static void validateToken(final String token, final PublicKey publicKey) throws ApiTokenProcessingException,
            ApiTokenMissingOrInvalidClaimsException, ApiTokenExpiredException, ApiTokenInvalidSignatureException {

        verifySignature(token, publicKey);

        Map<String, Object> claims = null;

        claims = TokenReader.readClaims(token, publicKey);

        // Are all expected fields present?
        Object iat = claims.get("iat");
        String applicationId = (String) claims.get(APPLICATION_ID_CLAIM);
        Object jti = claims.get("jti");
        Object exp = claims.get("exp");
        Object role = claims.get("role");
        boolean invalidApplicationId = false;

        try {
            UUIDParser.fromString(applicationId);
        } catch (RuntimeException e) {
            invalidApplicationId = true;
        }

        if (claims.size() != 5 || iat == null || isBlank(applicationId) || isBlank((String) jti) ||
                exp == null || isBlank((String) role) || invalidApplicationId) {
            throw new ApiTokenMissingOrInvalidClaimsException();
        }

        // Is the token expired?
        long expTime = (long) exp;
        if (expTime < System.currentTimeMillis()) {
            throw new ApiTokenExpiredException();
        }
    }

    private static void verifySignature(String token, PublicKey publicKey) throws ApiTokenInvalidSignatureException, ApiTokenProcessingException {
        try {
            TokenReader.verifySignature(token, publicKey);
        } catch (ApiTokenSignatureValidationException e) {
            throw new ApiTokenInvalidSignatureException();
        } catch (ApiTokenProcessingException e) {
            throw new ApiTokenProcessingException();
        }
    }
}
