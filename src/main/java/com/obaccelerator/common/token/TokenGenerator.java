package com.obaccelerator.common.token;

import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.obaccelerator.common.ObaConstant.CLIENT_ID_CLAIM;
import static com.obaccelerator.common.ObaConstant.ROLE_CLAIM;

@Slf4j
public class TokenGenerator {

    private static final long TEMP_TOKEN_VALIDITY_MS = 24 * 60 * 60 * 1000;

    private static final String ALGORITHM = AlgorithmIdentifiers.RSA_USING_SHA512;
    private static final long INTERNAL_TOKEN_VALIDITY_MS = 10 * 1000;
    private static final long API_TOKEN_VALIDITY_MS = 10 * 60 * 1000;

    private Key privateKey;

    public TokenGenerator(String pfxPath, String pfxPassword, String privateKeyAlias) {
        privateKey = new PfxUtil(pfxPath, pfxPassword).getPrivateKey(privateKeyAlias);
    }

    public String generateApiToken(String clientId, String externalClientRoleName) {
        return generateToken(API_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(CLIENT_ID_CLAIM, clientId);
                    put(ROLE_CLAIM, externalClientRoleName);
                }},
                Collections.emptyMap());
    }

    public String generateInternalToken(String clientId, String internalClientRoleName) {
        return generateToken(INTERNAL_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(CLIENT_ID_CLAIM, clientId);
                    put(ROLE_CLAIM, internalClientRoleName);
                }},
                Collections.emptyMap());
    }

    public String generateToken(String roleName) {
        return generateToken(API_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(ROLE_CLAIM, roleName);
                }},
                Collections.emptyMap());
    }

    public String generateInternalElevatedToken(String internalElevatedRoleName) {
        return generateToken(INTERNAL_TOKEN_VALIDITY_MS,
                new HashMap<String, String>() {{
                    put(ROLE_CLAIM, internalElevatedRoleName);
                }},
                Collections.emptyMap());
    }

    public String generateToken(long validityMs, Map<String, String> additionalClaims, Map<String, String> headers) {
        final long millis = System.currentTimeMillis();
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setClaim("iat", millis);
        jwtClaims.setClaim("exp", millis + TEMP_TOKEN_VALIDITY_MS); // valid for 10 minutes
        jwtClaims.setClaim("jti", UUID.randomUUID());
        for (String s : additionalClaims.keySet()) {
            jwtClaims.setClaim(s, additionalClaims.get(s));
        }
        return createTokenFromClaims(jwtClaims, headers);
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
