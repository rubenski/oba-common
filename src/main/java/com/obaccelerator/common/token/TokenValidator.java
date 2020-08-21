package com.obaccelerator.common.token;

import com.obaccelerator.common.uuid.UUIDParser;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.APPLICATION_ID_CLAIM;
import static com.obaccelerator.common.ObaConstant.ORGANIZATION_ID_CLAIM;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Validates API tokens that OBA created and provided to the client
 */
public class TokenValidator {

    private TokenValidator() {
    }

    public static void validateToken(final HttpServletRequest request, final PublicKey publicKey, String expectedRoleClaim) throws ApiTokenProcessingException,
            ApiTokenMissingOrInvalidClaimsException, ApiTokenExpiredException, ApiTokenInvalidSignatureException, ApiTokenMissingException, ApiTokenInvalidRoleClaimException {


        Optional<String> tokenOptional = TokenReader.getTokenFromHeader(request);
        if (tokenOptional.isEmpty()) {
            throw new ApiTokenMissingException();
        }

        validateToken(tokenOptional.get(), publicKey, expectedRoleClaim);
    }


    public static void validateToken(final String token, final PublicKey publicKey, String expectedRoleClaim) throws ApiTokenProcessingException,
            ApiTokenMissingOrInvalidClaimsException, ApiTokenExpiredException, ApiTokenInvalidSignatureException, ApiTokenInvalidRoleClaimException {

        verifySignature(token, publicKey);

        Map<String, Object> claims = null;

        claims = TokenReader.readClaims(token, publicKey);

        // Are all expected fields present?
        Object iat = claims.get("iat");
        String applicationId = (String) claims.get(APPLICATION_ID_CLAIM);
        String organizationId = (String) claims.get(ORGANIZATION_ID_CLAIM);
        Object jti = claims.get("jti");
        Object exp = claims.get("exp");
        Object role = claims.get("role");

        // Checking if provided ids are valid UUIDs.
        if (isNotBlank(applicationId)) {
            UUIDParser.fromString(applicationId);
        }

        if (isNotBlank(organizationId)) {
            UUIDParser.fromString(organizationId);
        }

        if (!expectedRoleClaim.equals((String) role)) {
            throw new ApiTokenInvalidRoleClaimException();
        }

        if (iat == null || (isBlank(applicationId) && isBlank(organizationId)) || isBlank((String) jti) ||
                exp == null) {
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
