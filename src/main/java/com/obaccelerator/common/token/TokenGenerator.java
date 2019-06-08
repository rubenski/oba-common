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
    public String createRequestToken(String clientId, String kid) {
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

    public String createInternalToken(String clientId, String userId) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("user_id", userId);
        jwtClaims.setClaim("iat", System.currentTimeMillis());
        jwtClaims.setClaim("exp", System.currentTimeMillis() + INTERNAL_TOKEN_VALIDITY_MS); // valid for 10s
        return createTokenFromClaims(jwtClaims);
    }

    public String createInternalToken(String clientId) {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", System.currentTimeMillis());
        jwtClaims.setClaim("exp", System.currentTimeMillis() + INTERNAL_TOKEN_VALIDITY_MS); // valid for 10s
        return createTokenFromClaims(jwtClaims);
    }

    public String createApiToken(String clientId, int validitySeconds) {
        final long millis = System.currentTimeMillis();
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("client_id", clientId);
        jwtClaims.setClaim("iat", millis);
        jwtClaims.setClaim("exp", millis + validitySeconds * 1000);
        jwtClaims.setClaim("jti", UUID.randomUUID());
        return createTokenFromClaims(jwtClaims);
    }


    private String createTokenFromClaims(JwtClaims jwtClaims) {
        String algorithm = AlgorithmIdentifiers.RSA_USING_SHA512;
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(jwtClaims.toJson());
        jws.setKey(privateKey);
        jws.setAlgorithmHeaderValue(algorithm);
        jws.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, algorithm));

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
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);

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
