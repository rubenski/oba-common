package com.obaccelerator.common.token;

import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
public class TokenGenerator {

    private static final String ALGORITHM = AlgorithmIdentifiers.RSA_USING_SHA512;


    private static final long INTERNAL_TOKEN_VALIDITY_MS = 10000;

    protected Key privateKey;

    public TokenGenerator(String pfxPath, String pfxPassword, String privateKeyAlias) {
        privateKey = new PfxUtil(pfxPath, pfxPassword).getPrivateKey(privateKeyAlias);
    }

    /**
     * There are some rules for client tokens. They must have an iat (issued at) claim. The issued at time cannot
     * be more than 20 seconds in the past. Also, it cannot be in the future. That is, we invalidate tokens on the Oba
     * side in order to prevent a client from losing a long-lived token.
     *
     * The jti (JWD IT) claim should be set as well. Oba should check that a token is only used once, but this is a
     * bit of a nice to have as the max validity period is already limited to only 20 seconds.
     *
     * The token must contain a key id header
     *
     * @param clientId id of the client
     * @return
     */
    public String generateRequestToken(String clientId, String kid) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", System.currentTimeMillis());
        jwtClaims.setClaim("jti", UUID.randomUUID());

        return createTokenFromClaims(jwtClaims, new HashMap<String, String>() {
            {
                put("kid", kid);
            }
        });
    }

    public String generateInternalToken(String clientId, String userId) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("user_id", userId);
        jwtClaims.setClaim("iat", System.currentTimeMillis());
        jwtClaims.setClaim("exp", System.currentTimeMillis() + INTERNAL_TOKEN_VALIDITY_MS); // valid for 10s
        return createTokenFromClaims(jwtClaims);
    }

    public String generateInternalToken(String clientId) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", System.currentTimeMillis());
        jwtClaims.setClaim("exp", System.currentTimeMillis() + INTERNAL_TOKEN_VALIDITY_MS); // valid for 10s
        return createTokenFromClaims(jwtClaims);
    }

    public String generateApiToken(String clientId, int validityMinutes) {
        final long millis = System.currentTimeMillis();
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", millis);
        jwtClaims.setClaim("exp", millis + validityMinutes * 60 *  1000);
        jwtClaims.setClaim("jti", UUID.randomUUID());
        return createTokenFromClaims(jwtClaims);
    }

    private String createTokenFromClaims(JwtClaims jwtClaims) {

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(ALGORITHM);
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, ALGORITHM));

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }

    private String createTokenFromClaims(JwtClaims jwtClaims, Map<String, String> headers) {
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(ALGORITHM);
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, ALGORITHM));

        for (String s : headers.keySet()) {
            jws.setHeader(s, headers.get(s));
        }

        try {
            return jws.getCompactSerialization();
        } catch (JoseException e) {
            throw new RuntimeException(e);
        }
    }
}
